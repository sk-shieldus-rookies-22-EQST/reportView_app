from fastapi import FastAPI, HTTPException
from fastapi.responses import FileResponse
from pydantic import BaseModel
from typing import Optional

app = FastAPI()

class SearchPrams(BaseModel):
    keyword: Optional[str] = None
    sdate: Optional[str] = None
    edate: Optional[str] = None
    theme: Optional[str] = None


# Endpoint: /auth/login
@app.post("/auth/login")
async def login():
    return {"status":True}

# Endpoint: /auth/find/id
@app.post("/auth/find/id")
async def find_id():
    return {"user_id": "admin"}

# Endpoint: /auth/find/pw
@app.post("/auth/find/pw")
async def find_pw():
    return {"status": True}

# Endpoint: /view/booklist
@app.post("/view/booklist")
async def view_report(params: Optional[SearchPrams] = None ):
    bookList = [{"id": 1, "title": "Sample 1123 Report", "price":"10000", "writer":"작자미상","write_date":"20221010"},
            {"id": 2, "title": "Sample ㄴㅇㅁ1 Report", "price":"11000", "writer":"작자","write_date":"20221010"},
            {"id": 3, "title": "Sample 12ㅇㄹ Report", "price":"12000", "writer":"김 아무개","write_date":"20221010"},
            {"id": 4, "title": "Sample ㅛ숫3 Report", "price":"18500", "writer":"토마스","write_date":"20221010"},
            {"id": 5, "title": "Sample 123ㅁㅇ3 Report", "price":"19000", "writer":"123","write_date":"20221010"},
            {"id": 6, "title": "Sample ㄴㅇㄹ4 Report", "price":"17000", "writer":"ㅁㄴㅇ","write_date":"20221010"},
            {"id": 7, "title": "Sample ㅁㅁ2ㅁㅇㅊ Report", "price":"20000", "writer":"ㄹ호","write_date":"20221010"},
            {"id": 8, "title": "Sample asd22 Report", "price":"10000", "writer":"윌리엄","write_date":"20221010"},
            {"id": 9, "title": "Sample tgbbt4 Report", "price":"15000", "writer":"4ㄱ5ㅅ","write_date":"20221010"},
            {"id": 10, "title": "Sample 6rds3 Report", "price":"20000", "writer":"작자미상","write_date":"20221015"}]

    filtered = bookList

    if params:
        print(params)
        if params.keyword:
            filtered = [book for book in filtered if params.keyword in book["title"]]
        if params.sdate:
            filtered = [book for book in filtered if params.sdate < book["write_date"] and params.edate > book["write_date"]]
        if params.theme:
            filtered
    return {"book_list":filtered}

# Endpoint: /view/bookdetail/{id}
@app.post("/view/bookdetail/{id}")
async def view_detail(id: int):
    return {"id":1, "price":"10000", "title":"책 제목", "writer":"작자 미상", "detail":"책 관련 내용"}

# Endpoint: /view/book/viewer
@app.get("/view/book/viewer")
async def download_file(id: int):
    if id < 1:
        raise HTTPException(status_code=400, detail="ID must be 1 or greater")

    # Example file path
    file_path = "file.pdf"
    
    try:
        return FileResponse(path=file_path, filename="file.pdf", media_type="application/pdf")
    except Exception as e:
        raise HTTPException(status_code=404, detail="File not found")

# Endpoint: /board/qna
@app.post("/board/qna")
async def view_board():
    return {"qna":[{"id":1,"title":"title 1","user_id":"admin"},
                   {"id":2,"title":"title 2","user_id":"admin"},
                   {"id":3,"title":"title 3","user_id":"admin1"},
                   {"id":4,"title":"title 4","user_id":"admin2"}]}

# Endpoint: /board/qna/{id}
@app.post("/board/qna/{id}")
async def view_qna(id: int):
    return {"id":1,"title":"1","content":"content", "comment":"comment","user_id":"qna writer","date":"2022-10-10"}

# Endpoint: /board/qna/write
@app.post("/board/qna/write")
async def write_qna():
    return {"status":True}

# Endpoint: /board/qna/comment
@app.post("/board/qna/comment")
async def write_comment():
    return {"status":True}

# Endpoint: /api/signup
@app.post("/api/signup")
async def signup_ok():
    return {"status":True}

# Endpoint: /purchase/cart
@app.post("/purchase/cart")
async def purchase_cart():
    return {"cart_id":1,
            "book_list":[{"book_id":1, "title":"title 1", "price":"10000"},
                        {"book_id":2, "title":"title 2", "price":"12000"},
                        {"book_id":3, "title":"title 3", "price":"15000"}],
            "total_price":"37000"}

# Endpoint: /purchase/process
@app.post("/purchase/proccess")
async def purchase_proccess():
    return {"status":"ok"}

# Endpoint: /user/info
@app.post("/user/info")
async def user_info():
    return {"user_id":"Server Admin"}

# Endpoint: /user/booklist
@app.post("/user/booklist")
async def user_booklist():
    return {"book_list":[{"book_id":1000020,"book_title":"title 1","book_img_path":"https://cdn.discordapp.com/attachments/1331425576678068254/1332257219923804211/EQST.png?ex=679498b9&is=67934739&hm=f96667a61d7d0f6998efb23b16960d4454c08863d0606b1a3dd9e215c2196fb7&"},
            {"book_id":1000020,"book_title":"title 2","book_img_path":"https://cdn.discordapp.com/attachments/1331425576678068254/1332257219923804211/EQST.png?ex=679498b9&is=67934739&hm=f96667a61d7d0f6998efb23b16960d4454c08863d0606b1a3dd9e215c2196fb7&"},
            {"book_id":1000021,"book_title":"title 3","book_img_path":"https://cdn.discordapp.com/attachments/1331425576678068254/1332257219923804211/EQST.png?ex=679498b9&is=67934739&hm=f96667a61d7d0f6998efb23b16960d4454c08863d0606b1a3dd9e215c2196fb7&"}]}

# Endpoint: /user/purchase
@app.post("/user/purchase")
async def user_info():
    return {"purchase":[{"id":1,"title":"title 1", "price":"10000", "date":"20151212"},
                        {"id":2,"title":"title 2", "price":"12000", "date":"20151212"},
                        {"id":3,"title":"title 3", "price":"13000", "date":"20151212"}]}

# Instructions to build and run the FastAPI server
# 1. Save this script as `main.py`.
# 2. Install FastAPI and Uvicorn using the following command:
#    pip install fastapi uvicorn
# 3. Run the FastAPI server with the command:
#    uvicorn main:app --reload
# 4. Access the API documentation at:
#    http://127.0.0.1:8000/docs
# 5. Use the API by sending requests to the endpoints defined above.
