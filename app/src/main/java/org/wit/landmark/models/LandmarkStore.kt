package org.wit.landmark.models

interface LandmarkStore {
    fun findAll(): List<LandmarkModel>
    fun create(landmark: LandmarkModel)
    fun update(landmark: LandmarkModel)
    fun findById(id:Long) : LandmarkModel?
    fun delete(landmark: LandmarkModel)
}