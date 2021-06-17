#**Запуск БД**

1. Docker-compose up -d

#**Запуск сервисов Payment Gate и Credit Gat**

1. Перейти в каталог .\gate-simulator
2. docker image build -t gate-simulator-app:1.0 .
3. docker-compose up -d