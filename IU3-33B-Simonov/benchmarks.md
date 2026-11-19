# Замеры производительности

Apple M4 Pro, OpenJDK 25.0.4.1, JVM с `-Xms512m -Xmx512m`.
На каждом размере использовался один и тот же набор разных случайных целых чисел
для всех реализаций. Набор строился заранее с фиксированным seed 33.

Каждая серия создаёт пустую структуру. Для очереди сначала добавляются все элементы,
затем все извлекаются. Для таблицы выполняются три прохода — вставка, поиск и удаление.
Для каждого варианта сделано 8 прогревочных и 16 измеряемых запусков.
Порядок вариантов сдвигается между запусками. Таблица показывает среднее время целой серии.

| Структура | Операция | N элементов | Среднее время, мс |
|---|---|---:|---:|
| PriorityQueueCustom (min) | add + poll | 10 000 | 0,859 |
| java.util.PriorityQueue (min) | add + poll | 10 000 | 0,799 |
| PriorityQueueCustom (max) | add + poll | 10 000 | 0,860 |
| java.util.PriorityQueue (max) | add + poll | 10 000 | 0,814 |
| HashMapCustom | put + get + remove | 10 000 | 0,523 |
| java.util.HashMap | put + get + remove | 10 000 | 0,306 |
| PriorityQueueCustom (min) | add + poll | 50 000 | 5,545 |
| java.util.PriorityQueue (min) | add + poll | 50 000 | 5,069 |
| PriorityQueueCustom (max) | add + poll | 50 000 | 5,499 |
| java.util.PriorityQueue (max) | add + poll | 50 000 | 5,272 |
| HashMapCustom | put + get + remove | 50 000 | 2,902 |
| java.util.HashMap | put + get + remove | 50 000 | 2,069 |
| PriorityQueueCustom (min) | add + poll | 100 000 | 13,942 |
| java.util.PriorityQueue (min) | add + poll | 100 000 | 12,745 |
| PriorityQueueCustom (max) | add + poll | 100 000 | 13,237 |
| java.util.PriorityQueue (max) | add + poll | 100 000 | 12,389 |
| HashMapCustom | put + get + remove | 100 000 | 6,002 |
| java.util.HashMap | put + get + remove | 100 000 | 5,218 |

На 100 000 элементов min-heap и max-heap показали близкое время: 13,942 и 13,237 мс.
У них одинаковая асимптотика, а порядок входа меняет число просеиваний и сравнений,
поэтому небольшая разница в этом замере не означает, что один режим всегда быстрее.
Стандартная хеш-таблица была быстрее на всех трёх размерах, а в собственной таблице
коллизии требуют последовательных проб.
Собственная таблица выделяет два массива и не создаёт объект узла на запись, у стандартной
таблицы есть массив корзин и объекты узлов. Фактическое потребление памяти не измерялось.

Из корня проекта:

```bash
mvn -pl IU3-33B-Simonov -am test-compile
java -Xms512m -Xmx512m -cp IU3-33B-Simonov/target/classes:IU3-33B-Simonov/target/test-classes ru.bmstu.lab02.Benchmarks
```
