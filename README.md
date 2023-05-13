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
- The project aims to develop a mobile application that helps pet users to connect to stores more conveniently. 
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
- [ ] User account for customers to view information about available or orderable pets in the shop
- [ ] After selecting and adding desired items to the cart, users can proceed to checkout
- [ ] Users can leave reviews for the shop
- [ ] Users can opt for pet sitting services if they need to send their pets to work
- [ ] Users can schedule health check-ups and grooming services for their pets
- [ ] The app will have a news section where customers can stay updated on pet-related information and knowledge
## 📚Project Stack
- **MVVM Architecture** 
- **Navigation Component**
- **ViewBinding**
- **ViewModel**
- **LiveData**
- **Coroutine**
- **Retrofit**
- **Glide**
- **Paging**
- **Firebase**
- **Node JS**
- **PostgreSQL**
- **Realm**
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
- [**Realm**](https://realm.io/)
- [**Paging**](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [**Firebase Auth**](https://firebase.google.com/docs/auth/android/start)
- [**Paging**](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

## 📋Project Assignment
Mỗi người sẽ làm việc trên Fragment, tự code UI (ở layout có tên tương ứng), chức năng của Fragment đó. Các tài nguyên chung (dữ liệu người dùng,database,...) thì bàn với nhóm và sẽ được đặt ở **MainActivity.kt**

###  📌Assignment 1
- **HomeFragment** : Giao diện trang chủ, hiển thị
  - Người làm: Quỳnh
- **ShoppingFragment** Giao diện mua hàng
  - Người làm: Hiện
- **SpaFragment + HotelFragment** : Giao diện spa + hotel
  - Người làm : Niên
- **ProfileFragment** : Giao diện hồ sơ người dùng 
  - Người làm : Dũng

### 📌 Assignment 2:
- **HomeFragment :** Quỳnh
  - Tạm thời code cứng link ảnh và link bài viết vào
  - Sử dụng **Glide** để cập nhật hình ảnh bằng link
  - setOnClickLinstener vào từng bước đến mở link bài báo
    - _Cách 1 (Dễ hơn)_: Khi nhấn vào hình -> mở trình duyệt dẫn đến link báo đó [Hướng dẫn mở browser từ android](https://stackoverflow.com/a/32174773/20864329)
    - _Cách 2_ : Tạo thêm một Fragment chứa WebView, nhấn vào hình -> gửi link vào Fragment đó-> Fragment hiển thị bài báo lên bằng WebView [Hướng dẫn WebView](https://stackoverflow.com/a/47872253/20864329)
 - **Spa & Hotel Fragment** : Niên
    - Sửa lại layout theo yêu cầu từ Dũng:
      - Pet : xóa các edit text, sửa thành một spinner
      - Owner: sửa thành các textview tham khảo Your Booking
    - Có thể sẽ gộp Spa và Hotel Fragment lại sử dụng **TabLayout** [Hướng dẫn TabLayout](https://youtu.be/pIKdHeOjYNw). Liên hệ Dũng để biết chi tiết
  - **Profile Fragment** : Dũng
    - Hoàn thành tất cả chức năng thêm, xóa, sửa pet
    - Tạo lớp User để lưu trữ thông tin người dùng gồm:
      - id: String =""
      - name: String = ""
      - email: String =""
      - phone: String =""
      - address: String =""
      - image: String =""
    - Khai báo biến MutableLiveData User trong InformationViewModel
    - Tạm thời code cứng thông tin User tại MainActivity (_Xem hướng dẫn ở onCreate()_)
    - ở **ProfileFragment** lấy thông tin user của View model và truyền thông tin lên UI
  - **ShoppingFragment** : Hiện
    - Lấy dữ liệu shop từ API
    - Sử dụng phân trang Paging
  
### 📌 Assignment 3:
 - **Home** : Quỳnh
    - [X] Thiết kế màn hình hóa đơn (sau khi thanh toán), danh sách lịch hẹn, chi tiết của một lịch hẹn (cho phần Spa và Hotel).
      - **Deadline : 11/04/20230**
    - Lấy dữ liệu news từ API: Sử dụng **HomeViewModel** ở ***HomeFragment***. 
      - **Deadline : 13/04/20230** 
  - **Spa/Hotel** : Niên
    - [X] Lập trình giao diện màn hình danh sách booking, chi tiết booking
      - **Deadline : 12/04/20230**
    - [ ] Kiểm tra dữ liệu nhập và gửi lịch đặt đến API. Làm theo hướng dẫn chi tiết tại **SpaFragment**
      - **Deadline : 15/04/20230**
  - **Order**: Dũng
    - [X] Chuẩn bị dữ liệu Pet:
      - **Deadline : 10/04/20230**
    - [X] cài đặt gọi API, Paging, lấy dữ liệu đưa giao diện, tham khảo **ProductPagingSource, productItemList trong ShoppingViewModel, ShoppingFragment** của Hiện, làm ở **ShoppingViewModel**  
      - **Deadline : 15/04/20230**
     - [X] Thực hiện lấy dữ liệu người dùng, (customerPickup), địa chỉ giao (deliveryAddress) ở **PetPaymentFragment**. Tham khảo và lấy các hàm gọi từ **PaymentFragment** 
        - **Deadline : 16/04/20230**
  - **Shopping** : Hiện
    - Thực hiện thanh toán (TH cash)
    - Thực hiện thanh toán Zalo Pay
### Assignment 4:
- **Pet Order**: Dũng  (**Deadline** : 22/04/2023)
  - Thực hiện mua và thanh toán pet:
    - Tạo lớp chứa dữ liệu để gửi lên API ở package apimodel (kế thừa từ **Order**) gồm các thông tin:
      - tất cả thông tin của lớp cha **Order**
      - var petID : String   _(chứa id của pet muốn mua)_
    - Sửa lại tham số của hàm ***sendPetOrder*** ở **PetIntoApi.kt**
    - viết hàm gọi API ở **ShoppingRepository**
    - Viết các hàm tạo va gửi pet Order (tham khảo hàm **createProductOrder** và **sendProductOrder**) ở **ShoppingViewModel**
    - viết hàm gửi order **sendPurchaseOrder** ở **PetPaymentFragment** tham khảo **PaymentFragment** (có thể debug để xem kết quả)
  - Thiết kế giao diện hóa đơn sau khi mua Pet (tham khảo **OrderPaymentFragment**)
- **Spa** : Niên  (**Deadline :** 20/04/2023)
  - Thực hiện tất cả TODO ở **SpaFragment**
 
## 📌Project Notes
- Trước khi làm **PULL**, làm xong một chức năng **PUSH**
- Chỉ làm việc trên Fragment của mình, có thể thêm các Activity, Fragment khác, ***Nhưng không được sửa phần Fragment khác***
- ***Không sửa MainActivity, các lớp chung, tài nguyên chung (colors.xml,string.xml,...)*** . Nếu muốn sửa phải bàn với nhóm
- Tham khảo sử dụng các UI components ở **res/layout/ui_components.xml** và sử dụng màu ở **res/values/colors.xml**
- Cập nhật thư viện sẽ thông báo trong nhóm, lúc đó thì pull mới về và chọn **File > Sync Project with Gradle Files**

##  📍 Project Progress
  ### 📱 Mobile
- [X] Set up Fragments navigation (Bottom navigation view)
- [X] Màn hình giao diện **Home**  : **Quỳnh**
  - [x] Ở mục Review chỉ dùng để hiển thị các đánh giá về App
  - [x] Ở mục Advertisement, phần chữ sẽ không bấm được, các hình ảnh về bài báo được hiển thị và khi bấm vào từng ảnh sẽ dẫn đến **WebView** hiển thị trang web bài báo
  - [X] Gọi API lấy tin tức từ Server
  - [ ] Thanh tìm kiếm sẽ dẫn tới tất cả các trang có thể phụ thuộc vào nội dung của text
  - [ ] Danh sách Pet khi nhấn vào từng Pet sẽ dẫn đến trang **Order** (fragment_order)
  - [ ] Nút **Order Now** cũng sẽ dẫn đến trang **Order** (fragment_order)
  - [ ] Ảnh ở mục Accesories chỉ để hiển thị hình ảnh 
  - [ ] **Chữ Accesories** ở mục Accesories khi bấm vào chuyển sang trang **Shopping**
  - [ ] Ảnh ở mục Beauty Spa chỉ để hiển thị hình ảnh
  - [ ] **Chữ Beauty Spa** ở mục Beauty Spa khi bấm vào chuyển sang trang **Spa**
  - [ ] Ảnh ở mục Hotel chỉ để hiển thị hình ảnh
  - [ ] **Chữ Hotel** ở mục Hotel khi bấm vào chuyển sang trang **Hotel**
- [ ] Màn hình giao diện **Shopping**  : **Hiện**
  - [X] Xây dựng giao diện
  - [X] Lấy dữ liệu từ API nạp vào RecycleView
  - [X] Cài đặt giao diện xem chi tiết sản phẩm (Xem ở **ShoppingDetails**)
  - [X] Giao diện giỏ hàng
  - [X] Thêm vào giỏ hàng
  - [X] Tính toán số lượng, tiền,...
  - [X] Bỏ khỏi giỏ hàng
  - [X] Lưu giỏ hàng vào local database (Realm)
  - [X] Paging
  - [X] Thanh toán
  - [X] Tìm kiếm (theo tên sản phẩm)
  - [ ] Filter
  - [X] Lịch sử mua hàng
- [X] Màn hình giao diện **Spa** và **Hotel** : **Niên**
  - [X] Xây dựng giao diện cho **SpaBooking** và **HotelBooking**
  - [X] Thực hiện đặt lịch cho Spa
  - [ ] Thực hiện đặt lịch cho Hotel
  - [ ] Nếu bấm delete thì sẽ xóa hết những thông tin đang hiển thị và trả về trạng thái không có thông tin gì ban đầu
  - [ ] Giao diện lịch sử đặt lịch, chi tiết
  - [ ] Hiển thị lịch sử đặt lịch
- [ ] Màn hình giao diện **Profile** và **Pet** : **Dũng**
  - [X] Xây dựng giao diện Profile
  - [X] Thêm, xóa, sửa thú nuôi của bản thân (local)
  - [X] Xây dựng giao diện **Pet**
  - [X] Gọi API hiển thị danh sách **Pet**
  - [X] Thanh toán mua Pet
  - [ ] Lịch sử mua Pet
- [X] Màn hình **Sign up /Sign in** : **Quỳnh**
  - [X] Đăng ký, đăng nhập  
  - [ ] Reset mật khẩu
  - [ ] Confirm email
- [X] Thay đổi ngôn ngữ : **Quỳnh**
- [ ] Test, kiểm lỗi
### Bonus
- [ ] Báo cáo lỗi app
- [ ] Chat GPT


### 💻 Web : **Hiện**
- [X] Set up server Node js
- [X] Xây dựng CSDL (PostgresSQL)
- [X] Chuẩn bị dữ liệu mẫu (Dũng, Quỳnh, Niên)
- [ ] Xây dựng API
- [X] Deploy web

### Các Bugs
- [ ] Ở trang Profile, khi bấm vào History hiện được trang history nhưng không đóng được.
- [ ] Tại mục Room type, trang Spa phần Hotel Booking sai giá cụ thể 200.00đ
- [ ] Phần chữ Hotel ở trang Home chưa được link tới Spa
- [ ] Khi bấm Delete ở trang Spa phần Hotel Booking thì total cost chưa được set về số 0đ
- [ ] Sau khi bấm vào từng lịch hẹn tại Booking trang Profile thì không thể bấm quay lại được cũng như bấm Cancel Booking thì cũng chỉ mới hiện thông báo chọn yes no chứ không có gì xảy ra.
- [ ] Tại trang Details khi mua sắm Accessories và chọn vào thông tin chi tiết sản phẩm button buy contact và số lượng không hoạt động có thể loại bỏ các nút.
- [ ] Không thanh toán được ở cả order và booking sau khi đã thêm địa chỉ mặc định