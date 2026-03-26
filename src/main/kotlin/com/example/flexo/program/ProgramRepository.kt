package com.example.flexo.program

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ProgramRepository : MongoRepository<Program, String>

