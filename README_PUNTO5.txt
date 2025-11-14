PUNTO 5 - READ COMMITTED

1. Abrir SQL Developer con 2 conexiones

2. CONEXION 1: ejecutar sesion1.sql
   - Primera consulta
   - Pausar

3. CONEXION 2: ejecutar sesion2.sql
   - Editar los IDs (conductor, vehiculo, servicio)
   - Ejecutar hasta COMMIT

4. CONEXION 1: presionar Enter
   - Segunda consulta debe mostrar +1

Resultado esperado:
- Primera consulta: 5
- Segunda consulta: 6

Esto muestra que READ COMMITTED permite ver cambios de otras transacciones.
