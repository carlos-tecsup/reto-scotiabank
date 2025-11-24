# Redis Integration

## Configuración

### Levantar Redis con Docker

```powershell
docker run -d --name redis -p 6379:6379 redis:7-alpine
```

### Verificar que Redis está corriendo

```powershell
docker ps
```

## Endpoints Redis

Los endpoints de Redis están disponibles en: `/api/redis/students`

### 1. Crear Estudiante en Redis

**POST** `/api/redis/students/create`

```json
{
  "id": 100,
  "name": "Carlos",
  "lastName": "Lopez",
  "age": 25
}
```

### 2. Buscar Estudiantes en Redis

**POST** `/api/redis/students/search`

```json
{
  "isActive": true
}
```

O enviar body vacío para obtener todos:

```json
{}
```

## Diferencias con H2

- **H2 (endpoints `/api/students`)**: Usa base de datos en memoria R2DBC
- **Redis (endpoints `/api/redis/students`)**: Usa Redis reactivo con TTL de 30 minutos

## Arquitectura

```
└── infraestructure/
    ├── config/
    │   └── RedisConfig.java (Configuración ReactiveRedisTemplate)
    ├── adapters/
    │   ├── input/rest/
    │   │   └── StudentRedisHandler.java (Handler para endpoints Redis)
    │   └── output/redis/
    │       ├── entity/
    │       │   └── StudentRedisEntity.java
    │       ├── mapper/
    │       │   └── StudentRedisMapper.java
    │       └── StudentRedisRepositoryAdapter.java (Implementa StudentRepositoryPort)
    └── application/service/
        └── StudentRedisService.java (Usa el adapter Redis)
```

## Notas

- Los datos en Redis tienen un TTL de 30 minutos
- Las claves siguen el patrón: `student:{id}`
- Se mantiene un set con todos los IDs: `students:all`
