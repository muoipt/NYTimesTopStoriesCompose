# NYTimesTopStoriesCompose  
A **MVI architecture** template with **Jetpack Compose**  

This is a simple news reader app that fetches and displays the top stories feed from **The New York Times**. The app is built entirely with **Kotlin** and **Jetpack Compose** for a modern and responsive user experience.  

---

## 🌟 Features  

- **Fetch Top Stories Articles**  
  Retrieve the latest top stories from [The New York Times API](https://developer.nytimes.com/apis).  

- **Bookmark Articles**  
  Save articles to a local database for quick access later.  

- **Open Articles in Browser**  
  View the full article in your preferred web browser directly from the app.  

---

## 📱 User Interface  

The app is designed with Jetpack Compose, ensuring a modern, declarative UI.  

### 📹 Demo  
[![Screen Recording](https://github.com/user-attachments/assets/49b19ee5-3cec-45c1-b7b3-31bac0a209f2)](https://github.com/user-attachments/assets/49b19ee5-3cec-45c1-b7b3-31bac0a209f2)  

---

## 🛠️ Tech Stack  

The app uses the following technologies and frameworks:  

- **Architecture**: MVI (Model-View-Intent) + MVVM (Model-View-ViewModel)  
- **UI**: Jetpack Compose  
- **Networking**: Retrofit, OkHttp, Moshi  
- **Local Storage**: Room Database  
- **Dependency Injection**: Hilt  
- **Asynchronous Programming**: Coroutines + Kotlin Flow  
- **Image Loading**: Coil  
- **Code Generation**: KSP  

---

## 🗂️ Module Dependencies  

Below is the dependency structure for the app's modules:  

![NYTArchitecture Diagram](https://github.com/user-attachments/assets/1bb73515-9aed-4ed5-834a-bf2a18892c4b)  

---

## 🏗️ Architecture  

The app is built following the **MVI architecture** pattern, combining it with **MVVM** for specific modules to:  
- Simplify state management.  
- Enhance modularity and separation of concerns.  
- Support testability and maintainability.  

---

Feel free to contribute or use this project as a template for your own **Jetpack Compose** applications.  
