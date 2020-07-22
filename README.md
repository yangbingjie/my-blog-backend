# my-blog-backend

> Sprint boot & My SQL

# API

## Article

### Save article

`post`  api/article/save

request body:

- author_id: int
- title: string
- content: string
- is_public: bool
- preview: string

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- article_id: int

### Remove article

`post` api/article/remove

request body:

- author_id: int
- article_id: int

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string

### Get article by id

`post` api/article/getArticleById

request body:

- article_id: int
- user_id: int

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- author_id: int
- title: string
- content: string
- is_public: bool
- view_count: int
- like_count: int
- star_count: int
- update_time: Date

### Search article by title

`post` api/article/searchArticleByTitle

request body:

- title: string

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - preview: string
  - author_id: int
  - title: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date

### Search article by author

`post` api/article/searchArticleByAuthor

request body:

- author_id: int

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - author_id: int
  - title: string
  - content: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date
  - preview: string

### Search article by tag

`post`

request body:

- tag_id

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - author_id: int
  - title: string
  - content: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date
  - preview: string

## Tag

### Add tag

`post`  api/tag/add

request body:

- tag_name: string

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- tag_id: int

### Remove tag

`post`  api/tag/remove

request body:

- tag_id: int

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string

### Add tag for article

`post`  api/tag/addTagForArticle

request body:

- tag_id: int

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string

## User

### Login

`post`  api/user/login

request body:

- username: string
- password: string

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string

### Logout

### Edit Profile

`post`   api/user/editProfile

request body:

- description: string
- username: string

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string

## File

`post`   api/file/cover

## Comment

### Add Comment

`post`   api/comment/add

request body:

- article_id: int
- reference_comment_id: int
- content: string

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string
- comment_id: int

### Remove Comment

`post`   api/comment/remove

request body:

- comment_id: int
- user_id: int

response body:

- errno: integer
  - 0: success
  - 1: error
- errmsg: string

## Message

