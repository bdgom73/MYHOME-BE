# myhomeServer
myhome app 스프링을 이용한 서버입니다.

## REST API
1. 게시판

- 게시물 리스트 가져오기
```c
URL /myApi/bbs2/[board}/get
Method Get

- PathVariable
@board Enum free, photo, video
- PathParam
@size int
@page int

- request data
[
  {
    id : 1,
    title : "test title",
    description "<p>Test is ...</p>",
    writer : "kij",
    writer_id : 1,
    nickname : "kij",
    rank : "admin",
    writer_avatar_url : null,
    categoryList : "FREE",
    video_url : null,
    videoType : "NONE",
    video_thumbnail : null,
    created : "2021-07-29 18:18:30",
    updated : "2021-07-29 18:18:30",
    views : 0,
    recommend : 0,
    imageList : [],
    commentDTOList : []
  },
  {
    .
    .
    .
  },
]
```
- 게시글 쓰기
```c
URL /myApi/bbs2/write
Method Post

@RequestHeader Authorization 

-PathParam
@title String
@description String
@keyword List<String>
@images[] MultipartFile[] 
  required : false
@video MultipartFile 
  required : false
@video_url String
  required : false
@category_type Enum free, photo, video
  required : false
  
- request data
@board_id Long
```

- 해당 게시물 데이터 가져오기
```c
URL /myApi/bbs2/view/{id}/{category} 
Method Get

-PathVariable
@id Long
@category Enum free, photo, video

- request data
{
  id : 1,
  title : "test title",
  description "<p>Test is ...</p>",
  writer : "kij",
  writer_id : 1,
  nickname : "kij",
  rank : "admin",
  writer_avatar_url : null,
  categoryList : "FREE",
  video_url : null,
  videoType : "NONE",
  video_thumbnail : null,
  created : "2021-07-29 18:18:30",
  updated : "2021-07-29 18:18:30",
  views : 0,
  recommend : 0,
  imageList : [],
  commentDTOList : []
}
```
- 게시글 쓰기
```c
URL /myApi/bbs2/write
Method Post

@RequestHeader Authorization 

-PathParam
@title String
@description String
@keyword List<String>
@images[] MultipartFile[] 
  required : false
@video MultipartFile 
  required : false
@video_url String
  required : false
@category_type Enum free, photo, video
  required : false
```

- 게시글 수정
```c
URL /myApi/bbs2/update
Method Put

@RequestHeader Authorization 

-PathParam
@category String
@id Long
@title String
@description String
@images[] MultipartFile[] 
  required : false
@pre_image Long[]
  required : false
```
- 게시글 삭제
```c
URL /myApi/bbs2/delete
Method Delete

-PathParam
@id Long

```
