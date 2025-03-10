# **Statement vs PreparedStatement en JDBC**

En JDBC, `Statement` y `PreparedStatement` son clases utilizadas para ejecutar consultas SQL, pero tienen diferencias clave en seguridad, rendimiento y uso.

---

## **1. Statement**

`Statement` se usa para ejecutar consultas SQL simples. No precompila las consultas y concatena directamente los valores dentro del SQL.

### Características de `Statement`

✅ Se usa para ejecutar consultas estáticas.\
❌ Es vulnerable a inyección SQL.\
❌ No es eficiente cuando se ejecuta múltiples veces con diferentes valores.

### Ejemplo de uso de `Statement`

```java
Statement stmt = connection.createStatement();
String username = "admin";
String password = "1234";
String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
ResultSet rs = stmt.executeQuery(query);
```

🔴 **Problema**: La consulta se crea concatenando directamente las variables, lo que permite ataques de inyección SQL.

---

## **2. PreparedStatement**

`PreparedStatement` permite consultas SQL **parametrizadas**, es decir, los valores se envían por separado en lugar de concatenarse en la consulta.

### Características de `PreparedStatement`

✅ Evita inyección SQL al parametrizar valores.\
✅ Mejora el rendimiento al reutilizar la consulta precompilada.\
✅ Se usa para ejecutar consultas repetitivas con distintos valores.

### Ejemplo de uso de `PreparedStatement`

```java
String query = "SELECT * FROM users WHERE username = ? AND password = ?";
PreparedStatement pstmt = connection.prepareStatement(query);
pstmt.setString(1, "admin");
pstmt.setString(2, "1234");
ResultSet rs = pstmt.executeQuery();
```

🔵 **Ventaja**: No concatena valores directamente en la consulta, evitando inyección SQL y mejorando la eficiencia.

---

## **Diferencias clave entre Statement y PreparedStatement**

| Característica  | `Statement`                                  | `PreparedStatement`                                         |
| --------------- | -------------------------------------------- | ----------------------------------------------------------- |
| **Seguridad**   | ❌ Vulnerable a inyección SQL.                | ✅ Protege contra inyección SQL.                             |
| **Rendimiento** | ❌ Recompila la consulta en cada ejecución.   | ✅ Precompila la consulta y la reutiliza.                    |
| **Uso**         | ❌ Útil solo para consultas únicas y simples. | ✅ Ideal para consultas repetitivas con diferentes valores.  |
| **Parámetros**  | ❌ Usa concatenación manual.                  | ✅ Usa marcadores `?` para pasar parámetros de forma segura. |
| **Legibilidad** | ❌ Difícil de mantener con múltiples valores. | ✅ Más limpio y fácil de entender.                           |

---

## Ejemplo práctico de vulnerabilidad con `Statement`

Si un usuario ingresa lo siguiente en un formulario:

- **Usuario:** `admin`
- **Contraseña:** `' OR '1'='1` (inyección SQL)

El código usando `Statement`:

```java
String username = "admin";
String password = "' OR '1'='1";
Statement stmt = connection.createStatement();
String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
ResultSet rs = stmt.executeQuery(query);
```

📌 **Consulta generada:**

```sql
SELECT * FROM users WHERE username = 'admin' AND password = '' OR '1'='1'
```

🔴 **Problema**: `'1'='1'` siempre es **verdadero**, por lo que el atacante obtiene acceso sin necesidad de una contraseña válida.

---

## **Cómo **`PreparedStatement`** soluciona este problema**

```java
String query = "SELECT * FROM users WHERE username = ? AND password = ?";
PreparedStatement pstmt = connection.prepareStatement(query);
pstmt.setString(1, "admin");
pstmt.setString(2, "' OR '1'='1");
ResultSet rs = pstmt.executeQuery();
```

📌 **Consulta generada de manera segura:**

```sql
SELECT * FROM users WHERE username = 'admin' AND password = ''' OR ''1''=''1'''
```

🔵 **Protección**: El valor ingresado es tratado como un **dato literal** y no como código SQL, evitando la inyección SQL.

---

### **Conclusión**

- **Usa **`Statement`** solo cuando realmente no te preocupen consultas dinámicas ni seguridad.**
- **Siempre usa **`PreparedStatement`** para protegerte contra inyección SQL y mejorar el rendimiento.**

