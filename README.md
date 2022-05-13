# MergeFiles
## Назначение программы
Программа сортирует произвольное количество файлов с целыми числами. При этом применяется сортировка слиянием. Данные записаны в столбик. Строки могут содержать любые символы. Считается, что строки в файлах пердварительно отсортированы. Не используются библиотечные функции сортировки. 
Результатом работы программы является новый файл с объедиененным содержимым входных файлов, отсортированным по возрастанию или убыванию путем сортировки слиянием.
## Особенности реализации
### Обработка некорректных данных
К некорректным данным относятся буквенные, пробельные и другие сивмолы, которые не могут быть преобразованы в целые числа. Также к некорректным данным относятся несортированные участки данных входных файлов. Для возвращения и дальнешей записи корректных данных в выходной файл исользуется класс `CorrectData`, а точнее метод класса `getCorrectInt()`, который игнорирует некорректные данные. Если корректных данных в файле больше нет этот метод возвращает `null`. Таким образом, некорректные данные оказываются потерянными.
### Сортировка больших файлов
Предложенная программа способна сортировать файлы, размер которых значительно превышает объем оперативной памяти. Это достигается за счет применения временных файлов, в которых хранится результат промежуточной сортировки, а также за счет использования буфера при чтении и записи файлов. Временные файлы после завершения работы JVM удаляются. 
### Обработка исключений
Ошибки обработаны так, чтобы минимизировать количество ситуаций, когда обработка входных файлов невозможна. Таким образом, приоритет отдается частичной обработке данных вместо завершения программы.  
### Версия JAVA и MAVEN
SDK 1.8.0_332 
MAVEN 3.6.3
## Инструкция по запуску
В папке target можно найти упакованный файл `MergeFiles-1.0-SNAPSHOT.jar`. Его можно запустить из консоли windows с параметрами. Консоль необходимо запускать из директории где находится файл jar, в этой же директории должны находится входные файлы и, в этой же директории будет создан или перезаписан файл с результатом. Примеры параметров:

+ *java -jar MergeFiles-1.0-SNAPSHOT.jar -a out.txt test1.txt test2.txt test3.txt test5.txt*

+ *java -jar MergeFiles-1.0-SNAPSHOT.jar out.txt test1.txt test2.txt test3.txt test5.txt test4.txt*

### Описание параметров
1. Необязательный параметр -a или -d задаёт порядок сортировки по возрастанию или убыванию соответсвенно. 
2. Второй параметр (или первый) задает название выходного файла. 
3. Последующие параметры перечисляют названия файлов для сортировки. 

