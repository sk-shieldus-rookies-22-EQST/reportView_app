# 개요

- 테스트를 위해서 사용하는 API 실행 파일
- FastAPI를 기반으로 만들어 동작함
- auth와 view에 대한 API를 제공하며 응답에 대한 처리 프로세스를 가지고 있지 않음

# 필수 요소

- main.py 실행
- FastAPI 설치
    - command prompt에서 실행
    - cmd: `pip install fastapi uvicorn`
- 실행 방법
    - command prompt에서 실행
    - cmd: `uvicorn main:app --host 0.0.0.0 --reload`
- 접근 링크
    - ip 주소 확인 후 8000번 포트 사용
    - ex). http://192.168.1.187:8000/