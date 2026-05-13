# Job4j TODO

Приложение "TODO список" на базе Spring Boot.

## Стек технологий

- Spring Boot 2.7.3
- Thymeleaf
- Bootstrap 5
- Hibernate 5.6.11
- PostgreSQL
- Liquibase

## Сборка

```bash
mvn clean package
```

## Запуск

```bash
mvn spring-boot:run
```

## База данных

Создайте базу данных PostgreSQL:

```sql
CREATE DATABASE todo;
```

Настройте подключение в `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Структура проекта

- `src/main/java/ru/job4j/todo/controller` - контроллеры
- `src/main/java/ru/job4j/todo/service` - сервисы
- `src/main/java/ru/job4j/todo/store` - персистенция
- `src/main/java/ru/job4j/todo/model` - модели
- `src/main/resources/templates` - Thymeleaf шаблоны
- `src/main/resources/db` - Liquibase миграции

## Функционал

- Просмотр всех задач
- Фильтрация: Все / Выполненные / Новые
- Добавление новых задач
- Просмотр деталей задачи
- Редактирование задачи
- Удаление задачи
- Отметка задачи как выполненной