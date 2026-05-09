# Kotlin Cheatsheet

## General

Kotlin is a **multi-paradigm** language — it supports both styles:

- **Object-oriented programming (OOP)** — classes, objects, interfaces. Organize code around things (e.g. `object AddBeanTab : Tab`)
- **Functional programming (FP)** — lambdas, functions as parameters, immutable state. Organize code around actions and data transformations (e.g. `onValueChange = { product = it }`)

Compose leans heavily on functional programming — UI is described as functions that take data and emit UI, rather than objects that mutate themselves.

---

## Variables

- `val` — read-only, cannot be reassigned after set (like `const` in JS or `let` in Swift)
- `var` — mutable, can be reassigned

Convention: prefer `val` by default, only use `var` when reassignment is needed.

---

## Types

- `String` — a sequence of characters (text). Must always have a value, can never be null.
- `String?` — a nullable String, can hold a String or `null`
- Any type can be made nullable with `?`: `Int?`, `Boolean?`, `BeansDatabase?`, etc.
- Kotlin forces you to handle the null case before using a nullable value — prevents `NullPointerException` common in Java

```kotlin
var name: String = "Alex"   // can never be null
var error: String? = null   // can be a String or null
```

**Nullable operators:**

| Operator | Name | Description |
|---|---|---|
| `?.` | Safe call | Skips if null, returns null: `name?.length` |
| `?: ` | Elvis operator | Provides a default if null: `name?.length ?: 0` |
| `!!` | Non-null assertion | Forces non-null, crashes if actually null — avoid |
| `?.let { }` | Safe let | Runs the block only if not null, `it` is the value |
| `if (x != null)` | Null check | Explicit null check, compiler smart-casts inside |

```kotlin
var name: String? = null
name?.length           // null — safe, no crash
name?.length ?: 0      // 0 — default when null
name!!.length          // NullPointerException — avoid
name?.let { it.length } // only runs if name is not null
```

---

## Functions

```kotlin
// basic function
fun greet(): String {
    return "Hello"
}

// single expression shorthand — no return keyword needed
fun greet(): String = "Hello"

// function with no return value (Unit = void in Java)
fun doSomething(): Unit { }

// Unit is the default so you can omit it
fun doSomething() { }
```

- `: TypeName` after the closing `)` declares the return type on a regular function
- `-> Unit` and `-> String` are **function type** notation — used when declaring a lambda parameter, not a regular function

```kotlin
// regular function return type uses :
fun greet(): String = "Hello"

// lambda parameter type uses ->
onSubmit: (String) -> Unit   // takes a String, returns nothing
```

**Extension functions** — add a new function to an existing type without modifying it. The type before the `.` is the **receiver** — the type being extended.

```kotlin
// extends RowScope with a new function
private fun RowScope.TabNavigationItem(tab: Tab) { ... }
```

- Can only be called where the receiver type exists (e.g. `RowScope.` means only inside a `Row` content block)
- Gets access to the receiver's properties and functions inside the body
- Doesn't modify the original class — just adds a new callable function to it

**Infix functions** — can be called between two values instead of dot notation:
```kotlin
LocalBeansDatabase provides database
// same as:
LocalBeansDatabase.provides(database)
```

---

## Lambdas

A lambda is an anonymous function passed as a value. Written with `{ }` syntax.

```kotlin
onValueChange = { brand = it }       // lambda assigned to a named parameter
onSubmit = { brand, product -> ... } // lambda with named parameters
```

- **`it`** — implicit name for a lambda's single parameter when you haven't named it
```kotlin
onValueChange = { brand = it }
// same as:
onValueChange = { newValue -> brand = newValue }
```

- **Trailing lambda** — if the last parameter is a lambda, it can be written outside the parentheses
```kotlin
Button(onClick = { ... }) {   // trailing lambda = the button's content
    Text("Add Bean")
}
```

**The three uses of `{ }` in Compose:**
- `parameter = { }` — event handler, called when something happens (click, text change)
- `Composable(...) { }` — content block, defines what goes visually inside the composable
- `fun name() { }` — regular Kotlin function body, not a lambda

**Content blocks vs regular trailing lambdas:**
- `@Composable` function + trailing `{ }` → **content block** — Compose renders whatever composables you put inside it
- Regular function + trailing `{ }` → **just a lambda** — does work like iterating, no UI rendered

```kotlin
Column(...) { Text("hello") }         // content block — renders UI inside Column
items(beans) { bean -> BeanCard(bean) } // regular lambda — just iterating, not a content block
```

**Static vs dynamic lambda parameters:**
```kotlin
// Static — you hand UI in, nothing comes back
bottomBar = { NavigationBar { ... } }

// Dynamic — the composable calculates a value and hands it back to you
) { innerPadding ->
    Surface(modifier = Modifier.padding(innerPadding)) { ... }
}
```

---

## Classes

**`object`** — creates a singleton: a class with exactly one instance. Accessed directly by name.
- A singleton does NOT mean it can only be rendered once — its functions can be called many times
- Think of it like a blueprint: one blueprint, used as many times as needed

**Interface** — a contract that defines what properties and functions a class must have, without providing the implementation.

**Implementing an interface** — use `:` followed by the interface name. Must implement all required members with `override`.

```kotlin
object AddBeanTab : Tab {
    override val key = "add_bean"
    override val options: TabOptions get() { ... }
    override fun Content() { ... }
}
```

**The `:` symbol has multiple meanings depending on context:**
- After a function's closing `)` → return type: `fun create(): BeansDatabase`
- After a variable name → type declaration: `var roastLevel: String?`
- After a class/object name → implements interface: `object AddBeanTab : Tab`
- After a class/object name → extends a class: `class MainActivity : ComponentActivity()`

**Extending a class vs implementing an interface:**
```kotlin
class MainActivity : ComponentActivity()  // extends a class — note the () to call its constructor
object AddBeanTab : Tab                   // implements an interface — no () needed
```
- Extending a class requires `()` to call the parent constructor
- Implementing an interface does not — interfaces have no constructor

---

## Annotations

- `@Composable` — marks a function as a UI function. It can call other composables and participates in recomposition (auto re-runs when its data changes). Only composables can call other composables — enforced by the compiler.

---

## Compose State

State is data that Compose watches — when it changes, Compose redraws the affected UI automatically.

- **`mutableStateOf(value)`** — creates a state holder. Compose watches it and triggers a redraw when the value changes.
- **`remember { ... }`** — keeps a value alive across recompositions. Without it, state resets every time the UI redraws.

These always go together:
```kotlin
var brand by remember { mutableStateOf("") }
// mutableStateOf — "watch this, redraw when it changes"
// remember — "keep this value alive between redraws"
```

**Property delegation (`by`)** — lets you read/write a state variable as a plain variable instead of using `.value`:
```kotlin
// without by — must use .value
var insertError = remember { mutableStateOf<String?>(null) }
insertError.value = "error"

// with by — reads and writes like a plain variable
var insertError by remember { mutableStateOf<String?>(null) }
insertError = "error"
```

`remember` only survives while the composable is on screen. Navigating away and back resets it.

**State Hoisting / Lifting State Up** — a pattern where a child composable takes in a lambda and calls it back up to the parent when something happens. The child doesn't know or care what happens with the data — the parent decides.

```
Parent
│
├── passes onSubmit lambda DOWN to Child
│
└── Child
    └── calls onSubmit back UP when button is tapped
```

```kotlin
// Parent passes lambda down
AddBeanForm(
    onSubmit = { brand, product, roastLevel, roastedDate ->
        database.beanQueries.insert(...)  // parent handles the data
    }
)

// Child calls lambda back up
Button(onClick = {
    onSubmit(brand, product, roastLevel, roastedDate)
})
```

---

## Flow

A Flow is a stream that emits new values over time, asynchronously. Similar to Java Streams but reactive — it keeps watching and pushing new data whenever something changes.

```kotlin
database.beanQueries.selectAll()
    .asFlow()                        // watch the query, emit on every DB change
    .mapToList(Dispatchers.IO)       // run on background thread, map to List<Bean>
    .collectAsState(emptyList())     // convert to Compose state, redraw UI on change
```

**How database changes trigger UI updates:**
1. `insert(...)` is called in `AddBeanTab`
2. SQLDelight detects the write and notifies the Flow
3. Flow emits a new `List<Bean>`
4. `collectAsState()` updates the `beans` state
5. Compose recomposes `BeanList` with the new data

No manual refresh needed — the UI stays in sync with the database automatically.

**Flow vs Java Stream:**
- Java Stream — processes existing data once, synchronous, runs once
- Kotlin Flow — emits values over time, asynchronous, keeps watching for changes

**Recomposition** only happens when state a composable reads changes — not on a timer, not constantly. Compose only recomposes the specific composables that read the changed state, not the entire screen.

---

## Coroutines

A coroutine is Kotlin's way of running code asynchronously — doing work in the background without blocking the main thread (which drives the UI).

```kotlin
scope.launch(Dispatchers.IO) {
    // runs on a background thread — UI doesn't freeze
    database.beanQueries.insert(...)
}
// continues immediately on the main thread
```

- **`scope.launch`** — fires a coroutine and returns immediately, code inside runs in the background
- **`Dispatchers.IO`** — background thread pool, use for database/network work
- **`Dispatchers.Main`** — main thread, use for UI updates
- **`rememberCoroutineScope()`** — creates a coroutine scope tied to a composable's lifecycle, automatically cancels running coroutines when the composable leaves the screen
- Coroutines look like normal sequential code but run asynchronously — simpler than Java threads or callbacks

---

## Standard Library Functions

- `error("message")` — throws an exception immediately with the given message. Used as a safety net to make misconfiguration obvious at runtime.
- `listOf(...)` — creates an immutable list: `listOf("Light", "Medium", "Dark")`
- `String.isBlank()` — returns true if the string is empty or only whitespace
- `String.trim()` — removes leading and trailing whitespace
- `String.takeIf { condition }` — returns the string if the condition is true, otherwise returns `null`
