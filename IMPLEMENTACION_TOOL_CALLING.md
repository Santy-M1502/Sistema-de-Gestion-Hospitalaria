# Implementación de Tool Calling - Guía Técnica

## Archivos Creados/Modificados

### Nuevos DTOs
- `AiToolCall.java` - DTO para representar acciones de la IA
- `AiToolResult.java` - DTO para resultados de acciones

### Nuevos Servicios
- `AiToolExecutor.java` - Ejecuta las 6 funciones disponibles
- `AiToolCallProcessor.java` - Procesa respuestas de IA con tool calls

### Servicios Modificados
- `PacienteChatService.java` - Integración de tool call processor
  - Inyecta `AiToolCallProcessor`
  - Incluye prompt de tools en primer mensaje
  - Procesa tool calls después de respuesta de IA

### Repository
- `TurnoRepository.java` - Agregado método `findByMedicoIdAndFechaBetween`

### Tests
- `AiToolExecutorTest.java` - Tests de validación básica

## Flow Técnico Detallado

```
ChatController.enviarMensaje([pacienteId], [mensaje])
    ↓
PacienteChatService.enviarMensaje()
    ├─ Busca paciente en BD
    ├─ Construye contexto clínico
    ├─ Si PRIMER MENSAJE:
    │  └─ Prepend prompt de tools (AiToolCallProcessor)
    ├─ Envía a AiChatClient (Ollama)
    │
    ├─ Ollama responde con posibles <tool>...</tool>
    │
    ├─ AiToolCallProcessor.procesarToolCalls()
    │  ├─ Extrae bloques <tool>...</tool>
    │  ├─ Para cada bloque:
    │  │  └─ AiToolExecutor.ejecutar()
    │  │     ├─ OBTENER_DISPONIBILIDAD_TURNOS
    │  │     ├─ AGENDAR_TURNO
    │  │     ├─ LISTAR_TURNOS_PACIENTE
    │  │     ├─ LISTAR_MEDICOS
    │  │     ├─ LISTAR_MEDICOS_POR_ESPECIALIDAD
    │  │     └─ CANCELAR_TURNO
    │  └─ Devuelve respuesta enriquecida
    │
    └─ Devuelve ChatResponse al frontend
```

## Estructura de Tool Call

Formato JSON que la IA devuelve dentro de `<tool>...</tool>`:

```json
{
  "tool": "OBTENER_DISPONIBILIDAD_TURNOS",
  "params": {
    "medicoId": 5,
    "fecha": "2025-04-15"
  }
}
```

## Las 6 Funciones Disponibles

### 1. OBTENER_DISPONIBILIDAD_TURNOS
```json
{
  "tool": "OBTENER_DISPONIBILIDAD_TURNOS",
  "params": {
    "medicoId": 5,
    "fecha": "2025-04-15"  // opcional
  }
}
```

### 2. AGENDAR_TURNO
```json
{
  "tool": "AGENDAR_TURNO",
  "params": {
    "medicoId": 5,
    "fecha": "2025-04-15",
    "hora": "14:30",
    "motivo": "Control general"  // opcional
  }
}
```

### 3. LISTAR_TURNOS_PACIENTE
```json
{
  "tool": "LISTAR_TURNOS_PACIENTE",
  "params": {}
}
```

### 4. LISTAR_MEDICOS
```json
{
  "tool": "LISTAR_MEDICOS",
  "params": {}
}
```

### 5. LISTAR_MEDICOS_POR_ESPECIALIDAD
```json
{
  "tool": "LISTAR_MEDICOS_POR_ESPECIALIDAD",
  "params": {
    "especialidad": "Cardiología"
  }
}
```

### 6. CANCELAR_TURNO
```json
{
  "tool": "CANCELAR_TURNO",
  "params": {
    "turnoId": 42
  }
}
```

## Compilación y Ejecución

```bash
# Compilar
mvn clean compile -DskipTests

# Ejecutar tests
mvn test

# Ejecutar aplicación
mvn spring-boot:run

# O directamente
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Testing Manual

### Test 1: Listar médicos
```bash
curl -X POST http://localhost:8089/api/chat/1/mensaje \
  -H "Content-Type: application/json" \
  -d '{
    "mensaje": "¿Qué médicos hay?",
    "incluirContextoClinico": true
  }'
```

### Test 2: Consultar disponibilidad
```bash
curl -X POST http://localhost:8089/api/chat/1/mensaje \
  -H "Content-Type: application/json" \
  -d '{
    "mensaje": "¿Qué días está disponible el Dr. García?",
    "incluirContextoClinico": true
  }'
```

## Debugging

### Ver logs de tool calls
```properties
logging.level.com.SGH.hospital.service.AiToolExecutor=DEBUG
logging.level.com.SGH.hospital.service.AiToolCallProcessor=DEBUG
```

### Extraer tool calls manualmente
```java
Pattern pattern = Pattern.compile("<tool>(.*?)</tool>", Pattern.DOTALL);
Matcher matcher = pattern.matcher(respuesta);
while (matcher.find()) {
    String json = matcher.group(1);
    // Parsear JSON...
}
```

## Posibles Errores

### "Tool call inválido"
- Verificar que `tool` y `params` no sean null
- Asegurar que el JSON sea válido

### "Médico no encontrado"
- Verificar que `medicoId` exista en BD
- Usar LISTAR_MEDICOS primero para obtener IDs

### "Error al agendar turno"
- Revisar formatos: fecha "YYYY-MM-DD", hora "HH:MM"
- Verificar que el paciente exista
- Asegurar que la hora esté en rango disponible (09:00-17:00)

## Configuración de Ollama

Para mejorar el rendimiento de tool calling en Ollama:

1. Usar modelo capaz: `mistral`, `neural-chat`, `llama2`
2. Aumentar `num_predict` para respuestas más largas
3. Ejemplos en system prompt ayudan mucho

## Notas Importantes

⚠️ El `@Transactional` en `AiToolExecutor.ejecutar()` es importante para consistencia

⚠️ Los tool calls se procesan síncronamente - Ollama espera respuesta

⚠️ Validar siempre parámetros, especialmente `medicoId` y fechas

⚠️ Testar en frontend con DevTools Network para ver rquests/responses completos

## Performance

- Consultas a BD están optimizadas con índices
- Tool calls se cachen si es posible
- Límite de slots por médico: 18 por día (09:00-17:00, intervalos 30 min)

## Seguridad

- Solo el paciente puede listar SUS turnos
- Solo el paciente puede cancelar sus propios turnos
- Las acciones se auditarían fácilmente via logs
