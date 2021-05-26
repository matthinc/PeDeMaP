package edu.hm.pedemap.map

import android.graphics.*

class TransactionalMultiBitmap(var size: Int) {
    private var tempTiles = HashMap<TileId, Bitmap>()
    private val tiles = HashMap<TileId, Bitmap>()

    fun resize(size: Int) {
        this.size = size
    }

    fun startTransaction() {
        tempTiles = HashMap<TileId, Bitmap>()
    }

    fun commitTransaction() {
        tiles.clear()
        tiles.putAll(tempTiles)
    }

    fun drawToCanvas(canvas: Canvas, destRect: Rect) {
        val partTileSize = ((destRect.width().toFloat() / size) * TILE_SIZE).toInt()
        val srcRect = Rect(0, 0, TILE_SIZE, TILE_SIZE)

        tiles.forEach {
            val tileX = destRect.left + partTileSize * it.key.x
            val tileY = destRect.top + partTileSize * it.key.y

            val partDestRect = Rect(
                tileX,
                tileY,
                tileX + partTileSize,
                tileY + partTileSize
            )

            canvas.drawBitmap(
                it.value,
                srcRect,
                partDestRect,
                Paint().also {
                    it.isFilterBitmap = false
                }
            )
        }
    }

    fun drawPath(path: List<PathPoint>, paint: Paint) {
        path.map { Pair(getTileIdForPoint(it.x.toInt(), it.y.toInt()), reservePoint(it.x.toInt(), it.y.toInt())) }
            .distinctBy { it.second }
            .forEach {
                val canvas = Canvas(it.second)
                canvas.drawPath(path.toPath(-it.first.x * TILE_SIZE, -it.first.y * TILE_SIZE), paint)
            }
    }

    private fun List<PathPoint>.toPath(offsetX: Int, offsetY: Int): Path {
        val actualPath = Path()
        actualPath.reset()

        val offsetPath = this.map { PathPoint(it.x + offsetX, it.y + offsetY) }

        var previousPathPoint = offsetPath.last()
        val startingPoint = offsetPath.last()

        actualPath.moveTo(startingPoint.x, startingPoint.y)

        offsetPath.forEach {
            actualPath.lineTo(previousPathPoint.x, previousPathPoint.y)
            previousPathPoint = it
        }

        return actualPath
    }

    /**
     * Create bitmap (if not exists) for a specific point
     */
    private fun reservePoint(x: Int, y: Int): Bitmap {
        val tileId = getTileIdForPoint(x, y)

        if (!tempTiles.containsKey(tileId)) {
            tempTiles[tileId] = Bitmap.createBitmap(TILE_SIZE, TILE_SIZE, Bitmap.Config.ARGB_8888)
        }

        return tempTiles[tileId]!!
    }

    /**
     * Returns the id of the tile on which the point with the given coordinates will be drawn.
     */
    private fun getTileIdForPoint(x: Int, y: Int): TileId {
        return TileId(x / TILE_SIZE, y / TILE_SIZE)
    }

    data class TileId(val x: Int, val y: Int)

    data class PathPoint(val x: Float, val y: Float)

    companion object {
        private const val TILE_SIZE = 256
    }
}
