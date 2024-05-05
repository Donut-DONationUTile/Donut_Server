## Introduction
[![Demo Video](https://github.com/akimcse/akimcse/assets/63237214/3aac2345-cb7c-4037-8ed8-a5fa99ef7fc3)](https://youtu.be/qjlmdKrCPaI)

  `DONUT` is a sustainable donation platform tailored to the developmental characteristics of adolescents, providing a stigma-free process for beneficiaries. </br></br>
  By utilizing unused resources such as gift vouchers, amounting to 900 million KRW annually in South Korea, it facilitates low-income youth to purchase groceries and essential items.



<br>


## Architecture

<image src='https://github.com/akimcse/akimcse/assets/63237214/49c749af-5579-42fe-b45c-d85a9c258dd8'/>
</br></br>

#### DONUT is comprised of a Client Application, Web Server, Database, Storage, and AI Server.
  The Client Application is implemented on `Android` using `Kotlin`, with the MVVM architecture to separate UI logic from business logic and increase code reusability and scalability. Through various interactions with users, the Client Application forwards requests to the Web Server.
  </br></br>
  The Web Server processes client requests and can communicate with the AI Server in need. It is implemented based on `Spring Boot` and utilizes `Redis` for JWT processing. Both Spring Boot and Redis are deployed in `Docker` containers via `GCP` Compute Engine. CI/CD is established using GCP Code Build and Artifact Registry for agile development.
  </br></br>
  A `MySQL` 8.0-based GCP Cloud SQL instance serves as the main Database. GCP Cloud Storage is used for the Image bucket, where URLs of inserted objects are stored in the Database.
  </br></br>
  The AI Model, served by `FastAPI`, is hosted on a separate VM from the Web Server. It is also deployed using Docker in preparation for utilizing `Kubernetes` for resource management caused by an increase in the number of users. Low-resolution gift card images forwarded from the Web Server to the AI Server are enhanced to high resolution using `TensorFlow`’s ESRGAN model and then stored in the GCP Storage bucket. The URLs of the stored objects are also updated in the Database.

</br></br>

## Tech Stacks
![image](https://github.com/Donut-DONationUTile/.github/assets/90603399/b35af4c6-eaf9-4588-aa71-587d2f13f2f2)


</br></br>

## How to Run - Server

### Requirement

- Account of Docker Hub

### Local
1. gradle build
2. docker build -t `yourAccount`/`yourRepository` ./
3. docker push `yourAccount`/`yourRepository`

### Deploy  
1. create docker-compose.yml
2. sudo docker pull `yourAccount`/`yourRepository`
3. sudo docker tag `yourAccount`/`yourRepository` `dockerImageName`
4. sudo docker-compose up

( [docker-compose.yml that we created is on Server repository](https://github.com/Donut-DONationUTile/Donut_Server/blob/main/docker-compose.yml))

</br></br>

## ✨ How to Use DONUT
<image src='https://github.com/akimcse/akimcse/assets/63237214/845d8d38-2b73-4897-ba63-c45ba32e28d0'/>

</br></br>

## Member

| <img src="https://avatars.githubusercontent.com/Ganghee-Lee-0522" width="80"> | <img src="https://avatars.githubusercontent.com/Kang1221" width="80"> |
|:---------------------------------------------------------------------------:|:------------------------------------------------------------------------:|
|                    [Ganghee Lee](https://github.com/Ganghee-Lee-0522)                    |                    [Yeonsoo Kang](https://github.com/Kang1221)                    |
|                   EWHA Womans University                                    |            EWHA Womans University                                        |
|                                   Backend                                   |                                 Backend                                  |


## Commit convention

| Tag name | Description                                                 |
| -------- | ----------------------------------------------------------- |
| feat     | Commits that add a new feature                              |
| fix      | Commits that fix a bug                                      |
| build    | Commits that affect build components                        |
| chore    | Miscellaneous commits                                       |
| style    | Commits for code styling or format                          |
| docs     | Commits that affect documentation only                      |
| test     | Commits that add missing tests or correcting existing tests |
| refactor | Commits for code refactoring                                |
