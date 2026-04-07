# Sistema de Tool Calling para Chat con IA

## Resumen

Se ha implementado un sistema de **Function Calling** que permite que la IA (Ollama) invoque endpoints de tu API para realizar acciones como:
- Consultar disponibilidad de turnos
- Agendar turnos
- Cancelar turnos
- Listar médicos
- Listar turnos del paciente

## Las 6 Funciones Disponibles

| Función | Descripción |
|---------|-----------|
| **OBTENER_DISPONIBILIDAD_TURNOS** | Consulta qué horarios están libres |
| **AGENDAR_TURNO** | Crea un nuevo turno |
| **LISTAR_TURNOS_PACIENTE** | Muestra tus turnos |
| **LISTAR_MEDICOS** | Lista todos los médicos |
| **LISTAR_MEDICOS_POR_ESPECIALIDAD** | Busca por especialidad |
| **CANCELAR_TURNO** | Cancela un turno existente |

## Componentes Principales

### DTOs
- `AiToolCall` - Representa una acción que la IA quiere ejecutar
- `AiToolResult` - Resultado de ejecutar una acción

### Servicios
- `AiToolExecutor` - Ejecuta cada tool call
- `AiToolCallProcessor` - Procesa respuestas de la IA, extrae y ejecuta tool calls
- `PacienteChatService` - (Actualizado) Integra el procesamiento de tools

### Repository
- `TurnoRepository` - Agregado método `findByMedicoIdAndFechaBetween`

## Cómo Funciona

### Flujo

```
1. Usuario envía mensaje
   ↓
2. En primer mensaje: Se incluye prompt con instrucciones de tools
   ↓
3. Ollama/IA recibe mensaje y contexto
   ↓
4. IA responde con bloques <tool>...</tool> si necesitaejecutar acciones
   ↓
5. AiToolCallProcessor extrae los bloques
   ↓
6. AiToolExecutor ejecuta cada acción
   ↓
7. Resultados se agregan a la respuesta finalizado
   ↓
8. Usuario ve respuesta completa con acciones realizadas
```

## Ejemplo de Uso

**Usuario:** "Quiero un turno para el 15 de abril"

**IA devuelve:**
```
Voy a verific la disponibilidad...

<tool>
{"tool": "LISTAR_MEDICOS", "params": {}}
</tool>

<tool>
{"tool": "OBTENER_DISPONIBILIDAD_TURNOS", "params": {"medicoId": 5, "fecha": "2025-04-15"}}
</tool>
```

**Backend procesa y devuelve:**
```
Voy a verificar la disponibilidad...

✓ Médicos disponibles:
- Dr. García (Cardiología) - ID: 5

✓ Disponibilidad para Dr. García el 2025-04-15:
09:00, 09:30, 10:00, 10:30, 14:00, 14:30, 15:00
```

## Testing

Se incluye `AiToolCallTest` con tests de:
- Validación de tool calls
- Conversión de parámetros
- Creación de resultados

## Configuración CORS

La aplicación ya tiene CORS configurado en:
- `CorsConfig.java`
- `application.properties` - Propiedad `cors.allowed-origins`

Asegúrate de que el frontend está en una URL permitida (ej: `http://localhost:3000`).

## Troubleshooting

**Error: Failed to fetch - CORS**
- Verifica que la URL sea `http://` o `https://`
- Revisa que el dominio del frontend esté en `cors.allowed-origins`
- Revisa la consola del navegador para más detalles

**La IA no invoca funciones**
- Comprueba que Ollama reciba el prompt completo con instrucciones de tools
- Verifica que el modelo sea capaz (no todos los modelos soportan bien tool calling)

**Error de compilación**
- Ejecuta `mvn clean compile`
- Verifica que todos los imports estén correctos

## Próximos Pasos

1. Probar con tu frontend
2. Ajustar el modelo de Ollama si es necesario
3. Agregar más funciones según sea necesario
