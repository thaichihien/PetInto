# Final Project - Pet Into 🐾
<hr>

- [Introduction](#project-introduction)
  - [Project Introduction](#project-introduction)
  - [Project Features](#project-features)
- [Project Management Area](#%EF%B8%8Fproject-version-tool)
  - [Project Version Tool](#%EF%B8%8Fproject-version-tool)
  - [Project Dependencies](#%EF%B8%8F-project-dependencies)
  - [Project Assignment](#project-assignment)
  - [Project Notes](#project-notes)
  - [Project Progress](#-project-progress)
<hr>

##  🐶Project Introduction
- The project aims to develop a mobile application that helps store owners better manage their pets and accompanying services, and help pet users to connect to those stores more conveniently. 
- The personal finance management app will be developed for Android platforms using Kotlin language. The app will have a simple and user-friendly interface that allows users to input their income and expenses, view reports, and set financial goals.
- [Get an initial overview of the project (Figma)](https://www.figma.com/file/9wqdJtfTQzIUkpJq1iSCom/Untitled?node-id=0%3A1&t=39MwhdTUMpNLvlgq-1)

<details>
    <summary> <b>Details</b> </summary>
    <ul>
        <li><b>Course</b> : CSC13009 – Mobile Application Development </li>
        <li><b>Group ID</b> : 9</li>
        <li><b>Group Name</b> : Mobye</li>
        <li> <b>Members:</b>
        <table style="width:100%">
  <tr >
    <th><b>ID</b></th>
    <th><b>Student ID</b></th> 
    <th><b>Fullname</b></th>
  </tr>
  <tr>
    <td>1</td>
    <td>20127473</td>
    <td>Vũ Đức Dũng</td>
  </tr>
  <tr>
    <td>2</td>
    <td>20127495</td>
    <td>Thái Chí Hiện</td>
  </tr>
  <tr>
    <td>3</td>
    <td>20127060</td>
    <td>Nguyễn Duy Niên</td>
  </tr>
   <tr>
    <td>4</td>
    <td>20127306</td>
    <td>Nguyễn Nhật Quỳnh</td>
  </tr>
</table></li>
    </ul>
    </details>
    
## 🐱Project Features
- [ ]  Admin account for shop owners to manage all customer notifications, appointments, create new pets, and publish news articles
- [ ] User account for customers to view information about available or orderable pets in the shop
- [ ] After selecting and adding desired items to the cart, users can proceed to checkout
- [ ] Users can leave reviews for the shop
- [ ] Users can opt for pet sitting services if they need to send their pets to work
- [ ] Users can schedule health check-ups and grooming services for their pets
- [ ] The app will have a news section where customers can stay updated on pet-related information and knowledge
- [ ]  Customers and shop owners can chat with each other
<hr>

**(Vietnamese for now)**
##  ⚙️Project Version Tool
Kiểm tra ở  ***Tools*** > ***SDK Manager***
- **Android Studio** : Electric Eel | 2022.1.1
- **SDK Platform:** Android 13.0 (Tiramisu)
- **Android SDK Build-Tools:** 33.0.1
- **Android SDK Platform-Tools:** 33.0.3
## 🗂️ Project Dependencies
Kiểm tra các thư viện/framework ở **Gradle Scripts** > ***build.gradle (Module:app)*** . Chọn ***Sync Now*** để tại các thư viện/framework về
- [**Navigation Component**](https://developer.android.com/guide/navigation/navigation-getting-started)
- [**Glide**](https://github.com/bumptech/glide)
- [**Retrofit**](https://github.com/square/retrofit)
- [**Lifecycle for View Model, Live Data**](https://developer.android.com/jetpack/androidx/releases/lifecycle)
## 📋Project Assignment
Mỗi người sẽ làm việc trên Fragment, tự code UI (ở layout có tên tương ứng), chức năng của Fragment đó. Các tài nguyên chung (dữ liệu người dùng,database,...) thì bàn với nhóm và sẽ được đặt ở **MainActivity.kt**
- **HomeFragment** : Giao diện trang chủ, hiển thị
  - Người làm: Quỳnh
- **ShoppingFragment** Giao diện mua hàng
  - Người làm: Hiện
- **SpaFragment + HotelFragment** : Giao diện spa + hotel
  - Người làm :
- **ProfileFragment** : Giao diện hồ sơ người dùng 
  - Người làm : Dũng
 
Nên hoàn thành sớm để tiếp tục cho phần đăng nhập/đăng ký
## 📌Project Notes
- Trước khi làm **PULL**, làm xong một chức năng **PUSH**
- Chỉ làm việc trên Fragment của mình, có thể thêm các Activity, Fragment khác, ***Nhưng không được sửa phần Fragment khác***
- ***Không sửa MainActivity, các lớp chung, tài nguyên chung (colors.xml,string.xml,...)*** . Nếu muốn sửa phải bàn với nhóm
- Tham khảo sử dụng các UI components ở **res/layout/ui_components.xml** và sử dụng màu ở **res/values/colors.xml**
- Cập nhật thư viện sẽ thông báo trong nhóm, lúc đó thì pull mới về và chọn **File > Sync Project with Gradle Files**

##  📍 Project Progress
  _* Dựa theo Figma_
  ### 📱 Mobile
- [X] Set up Fragments navigation (Bottom navigation view)
- [ ] Màn hình giao diện **Home** ( ***!!! Thông tin gì được thể hiện trên đây? dùng để làm gì? nhấn sẽ dẫn đến đâu?*** )
- [ ] Màn hình giao diện **Shopping** 
  - [X] Xây dựng giao diện
  - [ ] Lấy dữ liệu từ API nạp vào RecycleView
  - [X] Cài đặt giao diện xem chi tiết sản phẩm (Xem ở **ShoppingDetails**)
  - [X] Giao diện giỏ hàng
  - [X] Thêm vào giỏ hàng
  - [ ] Tính toán số lượng, tiền,...
  - [ ] Bỏ khỏi giỏ hàng
  - [ ] Lưu giỏ hàng vào local database (Realm)
  - [ ] Thanh toán?
  - [ ] Tìm kiếm (theo tên sản phẩm)
  - [ ] Filter
- [ ] Màn hình giao diện **Spa**
  - [ ] Xây dựng giao diện
  - [ ] Sau khi booking sẽ dẫn đến đâu ?
  - [ ] Làm sao để biết booking thành công ?
  - [ ] Có nên tự động fill vào thông tin theo tài khoản đã đăng nhập (đã có thông tin pet ở **Profile**)
- [ ] Màn hình giao diện **Hotel**
  - [ ] Xây dựng giao diện
  - [ ] Sau khi booking sẽ dẫn đến đâu ?
  - [ ] Làm sao để biết booking thành công ?
  - [ ] Có nên tự động fill vào thông tin theo tài khoản đã đăng nhập (đã có thông tin pet ở **Profile**)
- [ ] Màn hình giao diện **Profile**
  - [ ] Xây dựng giao diện
  - [ ] Giao diện **History**
  - [ ] Giao diện **Rate**
  - [ ] Giao diện **Order**/**Order Details**
  - [ ] Mục đích của Order ?
  - [ ] Giao diện **Booking**
  - [ ] Giao diện **Setting**
- [ ] Màn hình **Sign up /Sign in**
  - [ ] Giao diện **Sign up**
  - [ ] Tính năng đăng ký
  - [ ] Giao diện **Sign in**
  - [ ] Tính năng đăng nhập
  - [ ] Reset mật khẩu
- [ ] Giao diện Admin? (cần một app riêng biệt)

### 💻 Web
- [ ] Set up server Node js
- [ ] Xây dựng CSDL (PostgresSQL)
- [ ] Xây dựng API
- [ ] Deploy web

