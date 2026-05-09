# Database

Starting with the Bean table only. Espresso shots and drip brews can be added later.

## Schema

### Bean
| Column      | Type    | Notes                    |
| ----------- | ------- | ------------------------ |
| id          | integer | unique identifier        |
| dateAdded   | text    |                          |
| roastedDate | text    | nullable                 |
| brand       | text    |                          |
| product     | text    |                          |
| roastLevel  | text    | nullable; e.g. light, medium, dark |

### BeanFlavorTag
| Column | Type    | Notes                       |
| ------ | ------- | --------------------------- |
| id     | integer | unique identifier           |
| beanId | integer | links to Bean.id            |
| tag    | text    | e.g. "chocolate", "caramel" |

`(beanId, tag)` has a UNIQUE constraint — duplicate tags per bean are rejected at the DB level.

### BeanRating
| Column | Type    | Notes                          |
| ------ | ------- | ------------------------------ |
| id     | integer | unique identifier              |
| beanId | integer | links to Bean.id               |
| person | text    | who gave the rating            |
| rating | integer | 1–10                           |

Ratings are per-person rather than a single value on the bean. Both beanId FK columns cascade on delete.

## Example Query

Get all info for a bean including its flavor tags and ratings:

```sql
SELECT 
    bean.id,
    bean.brand,
    bean.product,
    bean.dateAdded,
    bean.roastedDate,
    bean.roastLevel,
    notes.tag,
    rating.person,
    rating.rating
FROM Bean bean
LEFT JOIN BeanFlavorTag notes ON bean.id = notes.beanId
LEFT JOIN BeanRating rating ON bean.id = rating.beanId
WHERE bean.id = N;
```

Returns one row per tag/rating combination — app code groups them into a single bean object with a list of tags and a map of person → rating.
