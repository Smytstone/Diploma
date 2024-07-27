# Руководство по запуску. 

1. Выполнить клонирование репозитория https://github.com/Smytstone/Diploma к себе на компьютер
2. Открыть проект в Intellij idea
3. Запустить контейнер командой docker compose up
4. В новой вкладке консоли перейти в папку artifacts командой cd artifacts
5. Запустить в этой вкладке приложение командой java -jar aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app (Приложение запускается на порту 8080)
6. В новой вкладке консоли запустить тесты командой .\gradlew clean test -DDB_URL=jdbc:postgresql://localhost:5432/app
7. Запустить репортинг командой ./gradlew allureServe
8. Остановить репортинг командой CTRL + C, нажать кнопку Y и ENTER
9. Остановить контейнер командой CTRL + C 

## Подключение к базе MySQL

1. Повторить шаги 1-4 из руководства по запуску.
2. Запустить в этой вкладке приложение командой java -jar aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app (Приложение запускается на порту 8080)
3. В новой вкладке консоли запустить тесты командой .\gradlew clean test -DDB_URL=jdbc:mysql://localhost:3306/app
4. Запустить репортинг командой ./gradlew allureServe
5. Повторить шаги 8-9 из руководства по запуску. 
