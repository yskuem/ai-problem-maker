# AI Problem Maker 🤖📚

AI-powered educational application that helps students learn by generating quizzes and notes from images of study materials.

## 🌟 Features

### 📚 Quiz Generation
- Take photos of textbooks, notes, or study materials
- AI automatically generates multiple-choice questions (4 options)
- Get explanations for correct answers
- Organize quizzes into projects

### 📝 Note Summarization
- Upload images of study materials or reference books
- AI extracts key points and creates organized HTML summaries
- Beautiful, colorful, and visually appealing layout
- Comprehensive content extraction without missing details

### 📁 Project Management
- Create projects to organize your quizzes and notes
- Search and filter projects
- Rename and delete projects
- Track last updated dates

### 🚀 Cross-Platform Support
- Native Android app
- Native iOS app
- Consistent user experience across platforms

## 🛠️ Technical Stack

### Frontend
- **Kotlin Multiplatform**: Shared business logic across platforms
- **Compose Multiplatform**: Modern UI framework
- **Voyager**: Navigation library
- **Koin**: Dependency injection
- **Ktor**: HTTP client for API communication

### Backend
- **Python FastAPI**: RESTful API server
- **Google Gemini 2.5 Flash**: AI model for quiz and note generation
- **Uvicorn**: ASGI server

### Database & Services
- **Supabase**: PostgreSQL database and authentication
- **Firebase**: Cloud storage, analytics, and crashlytics
- **Google Mobile Ads**: Monetization

### Architecture
- **Clean Architecture**: Separation of concerns with domain, data, and presentation layers
- **MVVM Pattern**: Model-View-ViewModel architecture
- **Repository Pattern**: Data access abstraction

## 🏗️ Project Structure

```
ai-problem-maker/
├── composeApp/                    # Kotlin Multiplatform app
│   ├── src/
│   │   ├── commonMain/           # Shared code
│   │   │   └── kotlin/
│   │   │       └── app/yskuem/aimondaimaker/
│   │   │           ├── core/     # Core utilities and UI components
│   │   │           ├── data/     # Data layer (repositories, API, database)
│   │   │           ├── domain/   # Business logic and entities
│   │   │           └── feature/  # Feature modules
│   │   ├── androidMain/          # Android-specific code
│   │   └── iosMain/              # iOS-specific code
├── iosApp/                       # iOS app configuration
├── server/                       # Python FastAPI backend
│   ├── main.py                   # Main server application
│   └── requirements.txt          # Python dependencies
└── icon/                         # App icons
```

## 🚀 Getting Started

### Prerequisites
- **Android Development**: Android Studio, Java 11+
- **iOS Development**: Xcode, macOS
- **Backend**: Python 3.9+, Google Gemini API key
- **Database**: Supabase account

### Backend Setup

1. **Install dependencies**
   ```bash
   cd server
   pip install -r requirements.txt
   ```

2. **Set environment variables**
   ```bash
   export GEMINI_API_KEY="your_gemini_api_key"
   ```

3. **Run the server**
   ```bash
   python main.py
   ```
   Server will be available at `http://localhost:8000`

### Mobile App Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yskuem/ai-problem-maker.git
   cd ai-problem-maker
   ```

2. **Configure Firebase**
   - Add `google-services.json` (Android) to `composeApp/src/dev/`
   - Add `GoogleService-Info.plist` (iOS) to `iosApp/GoogleService/dev/`

3. **Configure Supabase**
   - Update Supabase configuration in `composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/supabase/config/`

4. **Build and run**
   - **Android**: Open in Android Studio and run
   - **iOS**: Open `iosApp/iosApp.xcodeproj` in Xcode and run

## 🎯 Usage

### Creating a Quiz
1. Launch the app and create a new project
2. Select "Create Quiz from Image"
3. Take a photo or select from gallery
4. Wait for AI to generate questions
5. Review and study with the generated quiz

### Creating Notes
1. Create a new project
2. Select "Summarize Notes from Image"
3. Take a photo or select from gallery
4. Wait for AI to process the content
5. Review the generated HTML summary

## 🔧 Configuration

### Build Flavors
- **dev**: Development environment
- **staging**: Staging environment
- **prod**: Production environment

### API Endpoints
- `POST /generate_quizzes`: Generate quiz from image
- `POST /generate_note`: Generate note summary from image

## 📱 Supported Platforms

- **Android**: API 24+ (Android 7.0+)
- **iOS**: iOS 14.0+

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google Gemini**: AI model for content generation
- **Supabase**: Backend as a Service
- **Firebase**: Cloud services
- **JetBrains**: Kotlin Multiplatform and Compose Multiplatform

## 📞 Support

For questions or issues, please create an issue in the GitHub repository.

---

**Made with ❤️ for students who want to learn smarter, not harder.**