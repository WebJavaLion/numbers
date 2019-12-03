# numbers
Запуск: создать maven билд, собрать проект (clean install), запустить готовый jar (java -jar jarname.jar).
Или создать spring boot билд с указанием класса с main методом и запустить через IDE.
Запуск тестов: выполнить maven goal (clean test) или запускать через тест классы.

Зпросы к api: все запросы по localhost:12001/app. Для user - get (для получения списка юзеров), post: /users.  
put для апдейта по id: /users/{id}, delete: /users/{id}.Для поиска по имени /users/names/{name}. 

Для создания контакта - post: /users/{id}/contacts. Для получения всех контактов пользователя 
- get: /users/{id}/contacts. Для контаков: get (для всех контактов) - /contacts, для получения по id /contacts/{id}. Для апдейта контакта:
put - contacts/{id}, delete метод для удаления с таким же url. Для поиска контакта по номеру: get - contacts/numbers/{number}.


Для получения телефонных книжек: get(для получения всех книжек) - /books, для получения по id get - books/{id}, 
для получения пользователя по id книжки: get - books/users/{id}

get localhost:12001/app/users все пользователи
get localhost:12001/app/users/1 пользователь с id 1
delete localhost:12001/app/users/1 удаление пользователя с id 1
put localhost:12001/app/1 изменение пользователя с id 1
post localhost:12001/app/users создание нового пользователя
post localhost:12001/app/users/1/contacts создание нового контакта для пользователя с id 1
get localhost:12001/app/1/contacts получение всех контактов пользователя с id 1

get localhost:12001/app/contacts получение всех контактов
get localhost:12001/app/contacts/1 получение контакта с id 1
put localhost:12001/app/contacts/1 обновление контакта с id 1
delete localhost:12001/app/1 удаление контакта с id 1
get localhost:12001/app/contacts/numbers/8888888 получение контакта с номером 8888888

get localhost:12001/app/books для получения всех книжек
get localhost:12001/app/books/1 получение книжки с id 1
get localhost:12001/app/books/users/1 получение юзера, у которого id книжки 1

