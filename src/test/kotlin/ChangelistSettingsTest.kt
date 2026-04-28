import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.button
import com.intellij.driver.sdk.ui.components.elements.checkBoxWithName
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import com.intellij.ide.starter.sdk.JdkDownloaderFacade.jdk21
import kotlin.time.Duration.Companion.minutes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ChangelistSettingsTest {

    @Test
    fun `enable create changelists automatically`() {
        val testContext =
            Starter.newContext(
                    CurrentTestMethod.hyphenateWithClass(),
                    TestCase(
                        IdeProductProvider.IU,
                        GitHubProject.fromGithub(
                            branchName = "master",
                            repoRelativeUrl = "Perfecto-Quantum/Quantum-Starter-Kit.git",
                            commitHash = "1dc6128c115cb41fc442c088174e81f63406fad5",
                        ),
                    ),
                )
                .setupSdk(jdk21.toSdk())
                .setLicense(System.getenv("LICENSE_KEY"))
                .prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicators(5.minutes)

                openSettingsDialog()

                settingsDialog {
                    openTreeSettingsSection("Version Control", "Changelists")

                    content {
                        val checkbox = checkBoxWithName("Create changelists automatically")

                        if (!checkbox.isSelected()) {
                            checkbox.click()
                        }

                        assertTrue(checkbox.isSelected()) { "Checkbox is not selected" }
                    }

                    button("OK").click()
                }
            }
        }
    }
}
