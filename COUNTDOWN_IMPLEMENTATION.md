# 🎮 Lobby Countdown Implementation Guide

## 📋 Overview

Sistema de countdown implementado com **SSE (Server-Sent Events)** para notificações em tempo real quando um lobby atinge o número mínimo de jogadores.

---

## 🏗️ Arquitetura

```
Backend (SSE) → HttpLobbyService → LobbyViewModel → UI (Compose)
     ↓                ↓                  ↓              ↓
  Events          Flow<LobbyEvent>   StateFlow    CountdownDisplay
```

---

## 📦 Componentes Criados

### 1. **Domain Models**
- `LobbyCountdown.kt` - Representa o estado do countdown
- `LobbyEventDtos.kt` - DTOs para eventos SSE e union type `LobbyEvent`

### 2. **Service Layer**
- `HttpLobbyService.monitorLobbyEvents()` - Conexão SSE com backend
- Eventos suportados:
  - `CountdownStarted` - Countdown iniciado (min players atingido)
  - `GameStarted` - Jogo começou
  - `LobbyUpdated` - Lobby atualizado (players join/leave)

### 3. **ViewModel**
- `LobbyViewModel` - Gere countdown e eventos SSE
- Expõe:
  - `countdown: StateFlow<LobbyCountdown?>` - Estado do countdown
  - `gameStarted: StateFlow<Pair<Int, Int>?>` - Evento de game started

### 4. **UI Components**
- `CountdownUI.kt` - Composables para exibir countdown

---

## 🚀 Como Usar

### **1. No LobbyActivity/Screen**

```kotlin
@Composable
fun LobbyScreen(
    lobbyId: Int,
    viewModel: LobbyViewModel,
    onNavigateToGame: (gameId: Int) -> Unit
) {
    // Collect states
    val countdown by viewModel.countdown.collectAsState()
    val gameStarted by viewModel.gameStarted.collectAsState()
    
    // Start SSE monitoring when entering lobby
    LaunchedEffect(lobbyId) {
        viewModel.startMonitoringLobby(lobbyId)
    }
    
    // Auto-navigate to game when started
    LaunchedEffect(gameStarted) {
        gameStarted?.let { (lobbyId, gameId) ->
            onNavigateToGame(gameId)
        }
    }
    
    // Cleanup on exit
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopMonitoring()
        }
    }
    
    Column {
        // Your existing lobby UI
        LobbyInfo(lobby)
        PlayersList(players)
        
        // Show countdown when active
        countdown?.let { 
            CountdownDisplay(it)
        }
    }
}
```

---

## 📊 Fluxo de Eventos

### **Cenário 1: Countdown Inicia**

1. **Backend**: Player 2 joins → min players reached
2. **SSE Event**: `CountdownStarted { lobbyId: 1, expiresAt: 1734800000 }`
3. **Service**: Parse JSON → `LobbyEvent.CountdownStarted`
4. **ViewModel**: 
   - Recebe evento
   - Inicia countdown local (tick a cada 1s)
   - Atualiza `_countdown.value`
5. **UI**: Renderiza `CountdownDisplay` com timer

### **Cenário 2: Game Starts**

1. **Backend**: Countdown expires → game created
2. **SSE Event**: `GameStarted { lobbyId: 1, gameId: 42 }`
3. **ViewModel**: 
   - Para countdown
   - Emite `_gameStarted.value = (1, 42)`
4. **UI**: `LaunchedEffect` detecta mudança → navega para `GameActivity`

---

## 🔧 Configuração do Backend

O backend deve enviar eventos SSE neste formato:

```http
GET /api/lobbies/{lobbyId}/events
Authorization: Bearer {token}

# Response (SSE Stream):
event: CountdownStarted
data: {"lobbyId":1,"expiresAt":1734800000}

event: GameStarted
data: {"lobbyId":1,"gameId":42}

event: LobbyUpdated
data: {"id":1,"name":"My Lobby",...}
```

---

## 🎨 Personalização da UI

### **Estilo Minimalista**
```kotlin
@Composable
fun MinimalCountdown(countdown: LobbyCountdown) {
    Text(
        text = "Game starts in ${countdown.remainingSeconds}s",
        style = MaterialTheme.typography.bodyLarge
    )
}
```

### **Com Animação**
```kotlin
@Composable
fun AnimatedCountdown(countdown: LobbyCountdown) {
    val scale by animateFloatAsState(
        targetValue = if (countdown.remainingSeconds <= 3) 1.2f else 1f
    )
    
    Text(
        text = countdown.remainingSeconds.toString(),
        modifier = Modifier.scale(scale),
        style = MaterialTheme.typography.displayLarge,
        color = if (countdown.remainingSeconds <= 3) 
            Color.Red else MaterialTheme.colorScheme.primary
    )
}
```

---

## 🔍 Debugging

### **Ver eventos SSE no Logcat:**
```kotlin
// Em HttpLobbyService.kt
when (eventType) {
    "CountdownStarted" -> {
        println("🎯 Countdown started: $data")
        // ...
    }
}
```

### **Testar sem backend:**
```kotlin
// Mock countdown for testing
val testCountdown = LobbyCountdown(
    lobbyId = 1,
    expiresAt = System.currentTimeMillis() + 10_000,
    remainingSeconds = 10
)
_countdown.value = testCountdown
```

---

## ⚠️ Tratamento de Erros

### **Conexão SSE perdida:**
```kotlin
LobbyEvent.ConnectionLost → {
    // ViewModel para countdown
    // UI mostra mensagem de erro
    Snackbar("Connection lost. Retrying...")
}
```

### **Token expirado:**
```kotlin
// HttpLobbyService
if (token == null) {
    emit(LobbyEvent.ConnectionLost)
    // Redirecionar para login
}
```

---

## 🎯 Vantagens vs Polling

| Aspeto | SSE (Implementado) | Polling |
|--------|-------------------|---------|
| **Latência** | < 100ms | 1-2s |
| **Network Usage** | Baixo (push) | Alto (pull) |
| **Precisão** | 100% sincronizado | Pode dessincronizar |
| **Server Load** | Baixo | Alto |
| **Battery** | Melhor | Pior |

---

## 📚 Exemplos Completos

### **LobbyActivity Integration**

```kotlin
class LobbyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val app = application as PokerDice
        val viewModel: LobbyViewModel by viewModels {
            LobbyViewModel.getFactory(
                app.lobbyService,
                app.authInfoRepo,
                app.gameService
            )
        }
        
        val lobbyId = intent.getIntExtra("LOBBY_ID", -1)
        
        setContent {
            PokerDiceTheme {
                val countdown by viewModel.countdown.collectAsState()
                val gameStarted by viewModel.gameStarted.collectAsState()
                
                LaunchedEffect(lobbyId) {
                    viewModel.startMonitoringLobby(lobbyId)
                }
                
                LaunchedEffect(gameStarted) {
                    gameStarted?.let { (_, gameId) ->
                        navigateToGame(gameId)
                    }
                }
                
                LobbyScreenContent(
                    lobbyId = lobbyId,
                    countdown = countdown,
                    onLeave = { viewModel.leaveLobby(...) }
                )
            }
        }
    }
    
    private fun navigateToGame(gameId: Int) {
        startActivity(Intent(this, GameActivity::class.java).apply {
            putExtra("GAME_ID", gameId)
        })
        finish()
    }
}
```

---

## ✅ Checklist de Implementação

- [x] Adicionar dependência SSE Ktor
- [x] Criar domain models (LobbyCountdown, LobbyEvent)
- [x] Implementar SSE no HttpLobbyService
- [x] Adicionar countdown management no ViewModel
- [x] Criar UI components para countdown
- [ ] **Integrar no LobbyScreen** (próximo passo!)
- [ ] Testar com backend real
- [ ] Adicionar error handling na UI
- [ ] Implementar retry logic se SSE falhar

---

## 🐛 Troubleshooting

**Problema**: Countdown não aparece
- Verificar se backend envia evento `CountdownStarted`
- Verificar logs no Logcat: filtrar por "SSE" ou "Countdown"
- Verificar se `viewModel.startMonitoringLobby()` foi chamado

**Problema**: Countdown dessincroniza
- SSE deve ter prioridade sobre qualquer countdown local
- Verificar se `expiresAt` do backend está correto (timestamp em millis)

**Problema**: Não navega para o game
- Verificar se `LaunchedEffect(gameStarted)` está presente
- Verificar se backend envia evento `GameStarted` com `gameId` válido

---

## 🎓 Conceitos Avançados

### **Por que SSE em vez de WebSockets?**
- SSE é **unidirecional** (servidor → cliente) - perfeito para notificações
- Mais simples de implementar
- Reconnection automática
- Suportado nativamente por Ktor

### **Countdown Local vs Remoto**
- **Remoto**: `expiresAt` vem do servidor (source of truth)
- **Local**: Tick a cada 1s para UI smooth
- **Sincronização**: Se SSE enviar update, local reseta

---

## 🚀 Próximos Passos

1. **Integrar no LobbyScreen** - Adicionar `CountdownDisplay` na tua UI
2. **Testar com backend** - Verificar se eventos SSE funcionam
3. **Adicionar som/vibração** - Quando countdown chega a 3s
4. **Notificações** - Alertar user mesmo se app em background

Boa sorte! 🎮

