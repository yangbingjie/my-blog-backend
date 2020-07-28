# my-blog-backend

> Sprint boot & My SQL

# API

## Article

### Save article

`post`  api/article/save

request body:

- article_id: string
- author_id: string
- title: string
- content_html: string
- content_markdown: string
- is_public: bool
- preview: string
- img_folder: string
- img_list:[]
- tag_list:[{tag_id, tag_name}]

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article_id: string

### Edit auth

修改文章权限，公开改为私有，私有改为公开

`post` api/artilce/editAuth

request body:

- user_id: string
- article_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- is_public: int

### Remove article

`post` api/article/remove

request body:

- author_id: string
- article_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

### Get article by id

`post` api/article/getArticleById

request body:

- article_id: string
- user_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article:
  - author_name: string
  - title: string
  - content_html: string
  - content_markdown: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - is_like: bool
  - star_count: int
  - is_star: bool
  - update_time: Date
  - author_id：string
  - img_folder: string
  - tag_list:[{tag_id, tag_name}]

### Change like article state

`post` api/article/like

request body:

- article_id: string
- user_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- like_count: int
- is_like: bool

### Change star article state

`post` api/article/star

request body:

- article_id: string
- user_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- star_count: int
- is_star: bool

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
  - author_id: string
  - title: string
  - is_public: bool
  - view_count: int
  - like_count: int
  - star_count: int
  - update_time: Date

### Search article by author

`post` api/article/searchArticleByAuthor

request body:

- author_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- article_list: list
  - author_id: string
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
  - author_id: string
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
  - user_id: string
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

- article_id: string
- reference_comment_id: string
- content: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string
- comment_id: string

### Remove Comment

`post`   api/comment/remove

request body:

- comment_id: string
- user_id: string

response body:

- code: integer
  - 0: success
  - 1: error
- errmsg: string

## Message

