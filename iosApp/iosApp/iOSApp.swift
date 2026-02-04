import ComposeApp
import Firebase
import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

    class AppDelegate: NSObject, UIApplicationDelegate {
        func application(_: UIApplication, didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
            FirebaseApp.configure()
            RevenueCatInitializer.shared.configureIfNeeded()
            return true
        }
    }
}
