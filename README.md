# users_update_app
Это приложение - тесовое задание от компании ВА Системс.

Задание - написать приложение на java spring, файл настроек, в БД PostgreSQL одна таблица пользователей, у пользователя имя, возраст, должность, сделать эндпоийнты для создания пользователя, получения пользователя по id,  удаления пользователя по id.

#### Клонируйте репозиторий на локальный компьютер

'''git clone https://github.com/Ollegro/users_update_app.git'''

### Для большей уверенности, включите vpn - некоторые зависимости иначе не даются )) 

#### Запустите    

'''docker-compose up'''

#### для запуска в фоновом режиме      

'''docker-compose up -d'''

#### Ждите скачивания зависимостей и сборки. Приложение развернется в контейнере и запустится

#### Проверьте что контейнеры запущены

'''docker-compose ps'''

Настройте в IDEA соединение c БД. user - postgres , password - postgres , port - 5432 , db - mydatabase , host - localhost.

На момент запуска приложения таблица users в БД чистая.

Заполнить таблицу выполнить файл - init.sql (или не заполнять, а добавлять пользователей через POST запросы).

### Запросы:

Можно использовать PowerShell-команду Invoke-WebRequest или использовать Postman или Swagger.

Через PowerShell:

#### POST-запрос:
Для POST-запросов с телом JSON используйте параметр -Body:

'''  $body = @{
name = "Viktoria Sidorova"
age = 20
position = "Developer"
} | ConvertTo-Json   '''

потом запрос:  
'''   Invoke-WebRequest -Uri http://localhost:8080/users -Method POST -Body $body -ContentType "application/json"   '''


#### GET-запрос:  
Получение 1 пользователя  
'''   Invoke-WebRequest -Uri http://localhost:8080/users/1 -Method GET   '''

Получение всех пользователей  
'''   Invoke-WebRequest -Uri http://localhost:8080/users -Method GET  '''

#### DELETE-запрос:

Удаление 2 пользователя   
'''  Invoke-WebRequest -Uri http://localhost:8080/users/2 -Method DELETE  '''

Удаление всех пользователей   
'''  Invoke-WebRequest -Uri http://localhost:8080/users -Method DELETE  '''

#### Добавлены Unit тесты для проверки создания пользователя, работы сервиса, контроллера, репозитория.
Добавлена тестовая база данных h2