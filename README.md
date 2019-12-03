# numbers
Запуск: создать maven билд, собрать проект (clean install), запустить готовый jar (java -jar jarname.jar).
Или создать spring boot билд с указанием main метода и запустить через IDE.
Запуск тестов: выполнить maven goal (clean test) или запускать через тест классы.

Зпросы к api: все запросы по localhost:12001/app. Для user - get (для получения списка юзеров), post: /users.  
put для апдейта по id: /users/{id}, delete: /users/{id}.Для поиска по имени /users/names/{name}. 

Для создания контакта - post: /users/{id}/contacts. Для получения всех контактов пользователя 
- get: /users/{id}/contacts. Для контаков: get (для всех контактов) - /contacts, для получения по id /contacts/{id}. Для апдейта контакта:
put - contacts/{id}, delete метод для удаления с таким же url. Для поиска контакта по номеру: get - contacts/numbers/{number}.

Для получения телефонных книжек: get(для получения всех книжек) - /books, для получения по id get - books/{id}, 
для получения пользователя по id книжки: get - books/users/{id}
