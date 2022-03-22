# Дипломная Работа Яковлева Анна FQA-23

## Для запуска тестов необходимо иметь установленное ПО:
* Docker
* Java 11+
  
## Последовательность запуска тестов:
1. Склонировать репозиторий в локальную среду, выполнив в терминале `git clone https://github.com/AnnaYakovleva2302/diploma.git`
2. Открыть терминал, перейти по адресу папки с проектом 
3. Выполнить в терминале команду `chmod +x gradlew`
4. Выполнить в терминале команду `docker-compose up` - образ содержит 3 контейнера - mysql, postgresql и gate - simulator работы сервера.
5. Дождаться запуска сервисов docker и выполнить в новом окне терминала:
   * Вариант 1 - запуск с использованием БД MySQL: `java -jar aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/orders`
   * Вариант 2 - запуск с использованием БД PosgreSQL: `java -jar aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/orders`
6. Дождаться запуска jar приложения и выполнить в новом окне терминала 
   * Вариант 1 - запуск тестов, когда приложение подключено к БД MySQL: `./gradlew allureServe --rerun-tasks`
   * Вариант 2 - запуск тестов, когда приложение подключено к БД PosgreSQL: `./gradlew allureServe --rerun-tasks -Dspring.datasource.url=jdbc:postgresql://localhost:5432/orders`