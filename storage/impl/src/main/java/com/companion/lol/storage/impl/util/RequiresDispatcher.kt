package com.companion.lol.storage.impl.util

/**
 * Denotes that the annotated element performs synchronous, blocking operations (such as disk I/O,
 * database access, or heavy computation).
 *
 * This component is **not main-safe**. The caller is responsible for ensuring that this is invoked
 * within an appropriate background context, typically by using `withContext(Dispatchers.IO)` or
 * `withContext(Dispatchers.Default)`.
 *
 * The typical use-case is queries that need to perform inside a transaction, which by default in
 * sqldelight cannot be suspending functions
 */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.CLASS,
)
annotation class RequiresDispatcher
