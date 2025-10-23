# Hogwarts School Web Service

Веб-сервис для управления студентами и факультетами школы волшебства Хогвартс.

##  Описание

REST API сервис, реализующий базовые CRUD операции для работы со студентами и факультетами Хогвартса. Проект разработан в рамках учебного курса по Spring Boot.

##  Технологии

- **Java 17**
- **Spring Boot 3.5.6**
- **Maven**
- **Spring Web**
- **SpringDoc OpenAPI** (Swagger UI)
- **Spring Boot DevTools**

##  Быстрый старт

### Предварительные требования
- Java 17 или выше
- Maven 3.6+

### Запуск приложения

1. **Клонирование и сборка**
```bash
git clone <repository-url>
cd school
mvn clean install
```

2. **Запуск**
```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8080`

##  API документация

После запуска приложения доступна интерактивная документация:
- **Swagger UI**: http://localhost:8080/swagger-ui.html

##  API Endpoints

### Студенты (Student)

| Метод | URL | Описание |
|-------|-----|-----------|
| `GET` | `/student/{id}` | Получить студента по ID |
| `POST` | `/student` | Создать нового студента |
| `PUT` | `/student` | Обновить данные студента |
| `DELETE` | `/student/{id}` | Удалить студента |
| `GET` | `/student/age/{age}` | Найти студентов по возрасту |

### Факультеты (Faculty)

| Метод | URL | Описание |
|-------|-----|-----------|
| `GET` | `/faculty/{id}` | Получить факультет по ID |
| `POST` | `/faculty` | Создать новый факультет |
| `PUT` | `/faculty` | Обновить данные факультета |
| `DELETE` | `/faculty/{id}` | Удалить факультет |
| `GET` | `/faculty/color/{color}` | Найти факультеты по цвету |

##  Модели данных

### Student
```json
{
  "id": 1,
  "name": "Гарри Поттер",
  "age": 17
}
```

### Faculty
```json
{
  "id": 1,
  "name": "Гриффиндор",
  "color": "красный"
}
```

##  Структура проекта

```
src/main/java/ru/hogwarts/school/
├── model/           # Сущности предметной области
│   ├── Student.java
│   └── Faculty.java
├── service/         # Бизнес-логика
│   ├── StudentService.java
│   └── FacultyService.java
└── controller/      # REST API контроллеры
    ├── StudentController.java
    └── FacultyController.java
```

##  Тестирование

Для тестирования API используйте:
- **Swagger UI** - интерактивное тестирование через браузер
- **Postman** - коллекции запросов
- **cURL** - командная строка

### Пример запроса через cURL

**Создание студента:**
```bash
curl -X POST http://localhost:8080/student \
  -H "Content-Type: application/json" \
  -d '{"name": "Гермиона Грейнджер", "age": 17}'
```

##  Разработка

Проект использует Spring Boot DevTools для горячей перезагрузки при разработке. Изменения в коде автоматически применяются без перезапуска приложения.

##  Примечания

- Данные хранятся в памяти (HashMap) и сбрасываются при перезапуске приложения
- ID генерируются автоматически с помощью счетчика
- Проект будет расширяться в рамках учебного курса

---

*Разработано в рамках учебного курса по Spring Boot*
