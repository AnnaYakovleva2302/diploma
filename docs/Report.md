## Было проведено автоматизированное тестирование, которое включает следующие действия:
- покупка тура с использованием существующей карты из списка accepted
- покупка тура с использованием существующей карты из списка declined
- покупка тура с использованием несуществующей случайной карты
- покупка тура в кредит с использованием существующей карты из списка accepted
- покупка тура в кредит с использованием существующей карты из списка declined
- покупка тура в кредит с использованием несуществующей случайной карты
- попытка купить тур с пустой формой, проверка валидации полей

## Количество тест-кейсов
7

## % успешных / не успешных
- 42.85% успешных (3 из 7)
- 57.15% не успешных (4 из 7)

## Общие рекомендации
- Провести ревью UI дизайна - некоторые элементы затрудняют взаимодействие (например, кнопки переключения формы), некоторые накладываются друг на друга (например, сообщения об ошибке или успешной операции).
- Для тестирования взаимодействия приложения с базой данных требуются более детальные пояснения о принципах обработки транзакций в кредит на уровне базы данных (не понятно почему поле amount не присутствует в таблице кредита). 
- Также требуются пояснения о логике обработки транзакций по несуществующим картам - сообщение об ошибке то же что и для существующих Declined карт, но при этом не создается запись транзакции в отличии от транзакций с Declined картами.

## Скриншот отчета Allure
![image](https://user-images.githubusercontent.com/87671168/160436587-7ff8aeed-63c0-4bdc-8dd7-65f96682292e.png)
