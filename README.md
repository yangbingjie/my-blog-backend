# my-blog-backend

> Sprint boot & My SQL

# API

## Article

### Save article

`post`  api/article/save

request body:

- article_id: String
- author_id: String
- title: string
- content_html: string
- content_markdown: string
- is_public: bool
- preview: string
- img_list: []

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article_id: String

### Remove article

`post` api/article/remove

request body:

- author_id: String
- article_id: String

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

### Get article by id

`post` api/article/getArticleById

request body:

- article_id: String
- user_id: String

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article:
  - author_name: String
  - title: string
  - content_html: string
  - content_markdown: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date
  - author_id：String

### Search article by title

`post` api/article/searchArticleByTitle

request body:

- title: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - preview: string
  - author_id: String
  - title: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date

### Search article by author

`post` api/article/searchArticleByAuthor

request body:

- author_id: String

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - author_id: String
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

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - author_id: String
  - title: string
  - content: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date
  - preview: string

## File

## Add avatar

`post` api/file/uploadAvatar

response body:

- url: string

## Upload article img

`post` api/file/uploadArticleImg

param:

- folder: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- url: string
- folder: string

## Remove Article Img

`post` api/file/removeArticleImg

param:

- folder: string
- file_url: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

## Tag

### Add tag

`post`  api/tag/add

request body:

- tag_name: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- tag_id: String

### Remove tag

`post`  api/tag/remove

request body:

- tag_id: String

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

### Add tag for article

`post`  api/tag/addTagForArticle

request body:

- tag_id: String

response body:

- code: integer
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

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- user: 
  - user_id: String
  - role: int
  - avatar: string

### Logout

### Edit Profile

`post`   api/user/editProfile

request body:

- description: string
- username: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

## File

`post`   api/file/avatar

## Comment

### Add Comment

`post`   api/comment/add

request body:

- article_id: String
- reference_comment_id: String
- content: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- comment_id: String

### Remove Comment

`post`   api/comment/remove

request body:

- comment_id: String
- user_id: String

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

## Message

