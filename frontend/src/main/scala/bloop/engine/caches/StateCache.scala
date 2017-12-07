package bloop.engine.caches

import bloop.io.AbsolutePath
import java.util.concurrent.ConcurrentHashMap

import bloop.engine.State
final class StateCache(cache: ConcurrentHashMap[AbsolutePath, State]) {
  import bloop.util.JavaCompat.EnrichSAM
  def getBuildFor(path: AbsolutePath): Option[State] = Option(cache.get(path))
  def updateBuild(state: State): State = cache.put(state.build.origin, state)
  def addIfMissing(from: AbsolutePath, computeBuild: AbsolutePath => State): State =
    cache.computeIfAbsent(from, computeBuild.toJava)
  def allStates: Iterator[State] = {
    import scala.collection.JavaConverters._
    cache.asScala.valuesIterator
  }
}

object StateCache {
  def empty: StateCache = new StateCache(new ConcurrentHashMap())
}
