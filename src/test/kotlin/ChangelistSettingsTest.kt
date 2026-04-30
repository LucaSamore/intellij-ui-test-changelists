import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.button
import com.intellij.driver.sdk.ui.components.elements.checkBoxWithName
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IDETestContext
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import kotlin.time.Duration.Companion.minutes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ChangelistSettingsTest {

    @Test
    fun `enable create changelists automatically`() {
        configureTestContext().runIdeWithDriver().useDriverAndCloseIde {
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
                        assertTrue(checkbox.isSelected()) { "Checkbox should be selected" }
                    }
                    button("OK").click()
                }
            }
        }
    }

    @Test
    fun `setting is persisted after clicking OK and reopening settings`() {
        configureTestContext().runIdeWithDriver().useDriverAndCloseIde {
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
                    }
                    button("OK").click()
                }
                openSettingsDialog()
                settingsDialog {
                    val checkbox = checkBoxWithName("Create changelists automatically")
                    assertTrue(checkbox.isSelected()) {
                        "Checkbox should still be selected after closing and reopening settings"
                    }
                }
            }
        }
    }

    private fun configureTestContext(): IDETestContext =
        Starter.newContext(
                CurrentTestMethod.hyphenateWithClass(),
                TestCase(
                    ideInfo = IdeProductProvider.IU,
                    projectInfo =
                        GitHubProject.fromGithub(
                            branchName = "master",
                            repoRelativeUrl = "Perfecto-Quantum/Quantum-Starter-Kit.git",
                            commitHash = "1dc6128c115cb41fc442c088174e81f63406fad5",
                        ),
                ),
            )
            .setLicense(System.getenv("LICENSE_KEY"))
            .prepareProjectCleanImport()
}
