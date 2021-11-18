# File Upload REST Service

## Project description

Application provides REST service to upload and manage files

## What you can do:
- Add files (POST /file)
- Delete file (DELETE  /file/{ID})
- Assign tags (POST /file/{ID}/tags)
- Remove tags (DELETE /file/{ID}/tags)
- List files and filter them (GET /file?tags=tag1,tag2,tag3&page=2&size=3)

## How to run:

Application is build using Spring Boot Framework.\
Package manager: Maven\
Repository: Elasticsearch

To run application compile FileStorageService class
