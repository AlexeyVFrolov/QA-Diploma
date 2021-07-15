# **Проект автоматизации тестирования веб-сервиса**
## **Цель проекта**
Выполнение дипломной работы по курсу "Тестировщик ПО" - автоматизация сценариев тестирования функциональности приложения для покупки туристического тура. 

## **План автоматизации**
Сценарии для автоматизации, составленные на основе анализа задания, исследовательского тестирования данного веб-сервиса, перечень и назначение использованных инструментов автоматизации, анализ рисков проекта, оценка трудозатрат и календарный план проекта представлен в документе [Plan.md](https://github.com/AlexeyVFrolov/QA-Diploma/blob/master/documentation/Plan.md)

## **Отчет о результатах тестирования**
Анализ результатов тестирования веб-сервиса представлен в документе [Report.md](https://github.com/AlexeyVFrolov/QA-Diploma/blob/master/documentation/Report.md)

## **Итоги проекта**
Оценка результатов проекта: соответствие фактического объема работ запланированному, реализация рисков, выполнение календарного плана, а также анализ причин отклонений  представлены в документе [Summary.md](https://github.com/AlexeyVFrolov/QA-Diploma/blob/master/documentation/Summary.md)

## **Инструкция по запуску тестов**

1. Запуск БД и сервисов Payment Gate и Credit Gate: **Docker-compose up -d**
2. Запуск приложения для работы с MySQL: **java -jar -Dspring.datasource.url=jdbc:mysql://localhost:3306/app artifacts\aqa-shop.jar**
3. Запуск приложения для работы с PostgreSQL: **java -jar -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app artifacts\aqa-shop.jar**
4. Запуск тестов для работы с MySQL: **gradlew test -Ddatabase=mysql**
5. Запуск тестов для работы с PostgreSQL: **gradlew test -Ddatabase=postgresql**
6. Генерация отчетов Allure: **gradlew allureReport**

*Примечание:*  в пп 2,3 инструкции использован синтаксис для Windows