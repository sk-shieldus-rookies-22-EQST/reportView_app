from fastapi import FastAPI, HTTPException
from fastapi.responses import FileResponse
from pydantic import BaseModel
from typing import Optional
import random

app = FastAPI()

class SearchPrams(BaseModel):
    keyword: Optional[str] = None
    sdate: Optional[str] = None
    edate: Optional[str] = None
    theme: Optional[str] = None

book_img = [
    "https://marketplace.canva.com/EAGL2EOAvPo/2/0/1024w/canva-%EB%B2%A0%EC%9D%B4%EC%A7%80%EC%83%89-%EC%8B%AC%ED%94%8C%ED%95%9C-%EC%97%90%EC%84%B8%EC%9D%B4-%EC%86%8C%EC%84%A4-%EB%8F%84%EC%84%9C-%EC%B1%85%ED%91%9C%EC%A7%80-clHrySW7YqY.jpg",
    "https://marketplace.canva.com/EAF9dMIb-To/1/0/1003w/canva-%EA%B0%88%EC%83%89-%EB%AA%A8%EB%8D%98%ED%95%9C-%EC%84%B1%EA%B3%B5-%EC%9E%90%EA%B8%B0%EA%B3%84%EB%B0%9C-%EC%B1%85-%ED%91%9C%EC%A7%80-NG-ydDpeeV0.jpg",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPWC24U-sT4N4cY2qkBEDqui5ALJ9_Yd-Mha-NDXrokPw43w-aiwGnZOg_0WlXzwvI1u4&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYLdMPUeEPyiwAO7UUG_SUtLy6hWV3iR5Xppr9SGzEGuGbsGqPh5Hd4g_2nLtGMUuBGTA&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkvAqMvvtbiKjp2QZeH84UrfE2z7bWJbar73Z2rFcIpthg4dNqNnvhSKvlw8up1iQc7Bg&usqp=CAU",
    "https://template.canva.com/EAGNR5RVP94/1/0/501w-gsZqvY1af3k.jpg",
    "https://i0.wp.com/festbook.co.kr/wp-content/uploads/2022/12/%EB%B0%95%EC%B2%A0%ED%98%84_%EB%B3%B4%ED%97%98%EC%82%AC%EA%B8%B0_%EC%95%9E%ED%91%9C%EC%A7%80.png",
    "https://marketplace.canva.com/EAGLX9YW7t0/1/0/1003w/canva-%EB%82%A8%EC%83%89-%EC%98%A4%EB%A0%8C%EC%A7%80%EC%83%89-%EA%B9%94%EB%81%94%ED%95%9C-%EC%B1%85-%ED%91%9C%EC%A7%80-Lc8dEKJr9xM.jpg",
    "https://marketplace.canva.com/EAD161UHRIg/1/0/1003w/canva-%ED%8C%8C%EB%9E%80%EC%83%89-%EC%82%AC%EC%A7%84-%EA%B3%BC%ED%95%99-%EC%86%8C%EC%84%A4-%EC%B1%85-%ED%91%9C%EC%A7%80-W-oW2VKWuGo.jpg",
    "https://marketplace.canva.com/EAD15XOMDP8/1/0/1003w/canva-%ED%8C%8C%EB%9E%80%EC%83%89-%ED%95%98%EB%8A%98-%EA%B3%BC%ED%95%99-%EC%86%8C%EC%84%A4-%EC%B1%85-%ED%91%9C%EC%A7%80-tmAwAOxAkys.jpg",
    "https://marketplace.canva.com/EAD161UHRIg/1/0/1003w/canva-%ED%8C%8C%EB%9E%80%EC%83%89-%EC%82%AC%EC%A7%84-%EA%B3%BC%ED%95%99-%EC%86%8C%EC%84%A4-%EC%B1%85-%ED%91%9C%EC%A7%80-W-oW2VKWuGo.jpg",
    "https://ojsfile.ohmynews.com/STD_IMG_FILE/2018/0309/IE002297749_STD.jpg"
]


def create_book_list():
    return [{"book_id": 1, "title": "Sample 1123 Report", "price":"10000", "writer":"작자미상","write_date":"2025-01-10","book_img_path":random.choice(book_img)},
                {"book_id": 2, "title": "Sample 1124 Report", "price":"11000", "writer":"작자","write_date":"2025-01-10","book_img_path":random.choice(book_img)},
                {"book_id": 3, "title": "Sample 1212 Report", "price":"12000", "writer":"김 아무개","write_date":"2025-01-25","book_img_path":random.choice(book_img)},
                {"book_id": 4, "title": "Sample 1313 Report", "price":"18500", "writer":"토마스","write_date":"2025-01-25","book_img_path":random.choice(book_img)},
                {"book_id": 5, "title": "Sample 13 Report", "price":"19000", "writer":"123","write_date":"2025-01-15","book_img_path":random.choice(book_img)},
                {"book_id": 6, "title": "Sample 124 Report", "price":"17000", "writer":"ㅁㄴㅇ","write_date":"2025-01-15","book_img_path":random.choice(book_img)},
                {"book_id": 7, "title": "Sample asd Report", "price":"20000", "writer":"ㄹ호","write_date":"2025-01-20","book_img_path":random.choice(book_img)},
                {"book_id": 8, "title": "Sample asd22 Report", "price":"10000", "writer":"윌리엄","write_date":"2025-01-20","book_img_path":random.choice(book_img)},
                {"book_id": 9, "title": "Sample 333 Report", "price":"15000", "writer":"4ㄱ5ㅅ","write_date":"2025-01-30","book_img_path":random.choice(book_img)},
                {"book_id": 10, "title": "Sample 63 Report", "price":"20000", "writer":"작자미상","write_date":"2025-01-30","book_img_path":random.choice(book_img)}]

filtered = []

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
async def view_report():
    global filtered 
    filtered = create_book_list()

    return {"book_list":filtered}

# Endpoint: /view/booklist
@app.post("/view/search")
async def view_report(params: Optional[SearchPrams] = None ):
    global filtered 

    if params:
        print(params)
        if params.keyword:
            filtered = [book for book in filtered if params.keyword in book["title"]]
        if params.sdate:
            filtered = [book for book in filtered if params.sdate < book["write_date"] and params.edate > book["write_date"]]
        if len(filtered) == 0:
            filtered = create_book_list()
    return {"book_list":filtered}

# Endpoint: /view/bookdetail/{id}
@app.post("/view/bookdetail/{id}")
async def view_detail(id: int):
    global filtered
    book_detail = filtered[id-1]
    book_detail["detail"] = f"{id} 번호의 책"
    return book_detail

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
    return {"qna":[{"board_id":1,"title":"title 1","user_id":"admin"},
                   {"board_id":2,"title":"title 2","user_id":"admin"},
                   {"board_id":3,"title":"title 3","user_id":"admin1"},
                   {"board_id":4,"title":"title 4","user_id":"admin2"}]}

# Endpoint: /board/modify
@app.post("/board/modify")
async def modify_QnA():
    return {"status":True}


# Endpoint: /board/delete
@app.post("/board/delete")
async def delete_QnA():
    return {"status":True}


# Endpoint: /board/qna/{id}
@app.post("/board/qna/{id}")
async def view_qna(id: int):
    qna_dict = {1:{"board_id":1,"title":"title 1","content":"content", "comment":"comment","user_id":"qna writer","date":"2022-10-10"},
    2:{"board_id":2,"title":"title 2","content":"content", "comment":"comment","user_id":"qna writer","date":"2022-10-10"},
    3:{"board_id":3,"title":"title 3","content":"content", "comment":"comment","user_id":"qna writer","date":"2022-10-10"},
    4:{"board_id":4,"title":"title 4","content":"content", "comment":"comment","user_id":"qna writer","date":"2022-10-10"}}
    return qna_dict[id]

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
            }

# Endpoint: /purchase/proess
@app.post("/purchase/delete")
async def purchase_delete():
    return {"status":True}

# Endpoint: /purchase/item
@app.post("/purchase/item")
async def purchase_item():
    return {"status":True}

# Endpoint: /purchase/proess
@app.post("/purchase/process")
async def purchase_proccess():
    return {"status":True}

# Endpoint: /user/info
@app.post("/user/info")
async def user_info():
    return {"user_id":"Server Admin"}

# Endpoint: /user/booklist
@app.post("/user/booklist")
async def user_booklist():
    return {"book_list":[{"book_id":1000020,"book_title":"title 1","book_img_path":random.choice(book_img)},
            {"book_id":1000020,"book_title":"title 2","book_img_path":random.choice(book_img)},
            {"book_id":1000021,"book_title":"title 3","book_img_path":random.choice(book_img)}]}

# Endpoint: /user/purchase
@app.post("/user/purchase")
async def user_info():
    return {"purchase":[{"id":1,"title":"title 1", "price":10000, "date":"20151212"},
                        {"id":2,"title":"title 2", "price":12000, "date":"20151212"},
                        {"id":3,"title":"title 3", "price":13000, "date":"20151212"}]}




# Instructions to build and run the FastAPI server
# 1. Save this script as `main.py`.
# 2. Install FastAPI and Uvicorn using the following command:
#    pip install fastapi uvicorn
# 3. Run the FastAPI server with the command:
#    uvicorn main:app --reload
# 4. Access the API documentation at:
#    http://127.0.0.1:8000/docs
# 5. Use the API by sending requests to the endpoints defined above.
