package hcm.tests.case1;

import static util.ReportLogger.logFailure;
import static util.ReportLogger.log;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import common.BaseTest;
import common.ExcelUtilities;
import common.TaskUtilities;
import hcm.pageobjects.FuseWelcomePage;
import hcm.pageobjects.LoginPage;
import hcm.pageobjects.OracleIdentityManagerPage;
import hcm.pageobjects.TaskListManagerTopPage;

public class CreateImplementationUserTest extends BaseTest{
	private static final int MAX_TIME_OUT = 30;
	private String searchData, labelLocator, labelLocatorPath;
	
	private String projectName = "Default";
	
	private int projectCol = 8;
	private int label = 10;
	private int inputs = 11;
	private int colNum = 9;
	private int projectRowNum = TestCaseRow;
	private int projectSheetcolNum = 7;
	
	private boolean hasCreatedUsers = false;
	
	@Test
	public void a_test() throws Exception  {
		testReportFormat();
	
	try{	
			
		createImplementationUsers();
	  
	  	}
	
        catch (AssertionError ae)
        {
            takeScreenshot();
            logFailure(ae.getMessage());

            throw ae;
        }
        catch (Exception e)
        {
            takeScreenshot();
            logFailure(e.getMessage());

            throw e;
        }
    }

	public void createImplementationUsers() throws Exception{
		
		LoginPage login = new LoginPage(driver);
		takeScreenshot();
		login.enterUserID(5);
		login.enterPassword(6);
		login.clickSignInButton();
		
		FuseWelcomePage welcome = new FuseWelcomePage(driver);
		takeScreenshot();
		welcome.clickNavigator("More...");
		clickNavigationLink("Setup and Maintenance");
			
		TaskListManagerTopPage task = new TaskListManagerTopPage(driver);
		takeScreenshot();
		
		//final String ProjectSheetName = "Create Implementation Project";
		
		while(!hasCreatedUsers && !projectName.isEmpty() && !projectName.contentEquals("")){
			projectName = selectProjectName();
			
			if(projectName.contains("*")){
				projectRowNum += 1;
				continue;
			}
			
			hasCreatedUsers = createImpUsers(task);
		}
		
		log("User Data Roles has been set and created..");
		System.out.println("User Data Roles has been set and created..");
			
	}

	private String selectProjectName() throws Exception{
		System.out.println("Setting Project to be edited...RowNum: "+projectRowNum+" vs. "+TestCaseRow);
		final String projectSheetName = "Create Implementation Project";

		XSSFSheet projectSheet = ExcelUtilities.ExcelWBook.getSheet(projectSheetName);
		XSSFCell projectCell;
		String newProjectName ="";
		
		if(projectRowNum <= 0){
			projectRowNum = TestCaseRow;
		}
		
	  	try{	        	   
	  		projectCell = projectSheet.getRow(projectRowNum).getCell(projectSheetcolNum);      	  
	  		projectCell.setCellType(projectCell.CELL_TYPE_STRING);
	  		newProjectName = projectCell.getStringCellValue();
	            
	            }catch (Exception e){
	            	e.printStackTrace();
	            	newProjectName="";
	            }
	  	
		System.out.println("New Project Name is now..."+newProjectName);
				
		return newProjectName;
	}
	
	public boolean createImpUsers(TaskListManagerTopPage task) throws Exception{
		
		TaskUtilities.customWaitForElementVisibility("//a[text()='Manage Implementation Projects']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//a[text()='Manage Implementation Projects']");
		TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Implementation Projects']", MAX_TIME_OUT);
		
		searchData = projectName;
		labelLocator = "Name";
		labelLocatorPath = TaskUtilities.retryingSearchInput(labelLocator);
		
		TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, searchData);
		TaskUtilities.jsFindThenClick("//button[text()='Search']");
		Thread.sleep(3500);
		TaskUtilities.customWaitForElementVisibility("//a[text()='"+searchData+"']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//a[text()='"+searchData+"']");

		TaskUtilities.customWaitForElementVisibility("//h1[contains(text(),'"+searchData+"')]", MAX_TIME_OUT);
		TaskUtilities.customWaitForElementVisibility("//div[text()='Workforce Deployment']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Workforce Deployment']"+"//a[@title='Expand']", "xpath")){
			//TaskUtilities.jsFindThenClick("//div[text()='Workforce Deployment']"+"//a[@title='Expand']");
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Workforce Deployment']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Workforce Deployment']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Define Common Applications Configuration for Human Capital Management']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Expand']", "xpath")){
			//TaskUtilities.jsFindThenClick("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Expand']");
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Define Implementation Users']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Define Implementation Users']"+"//a[@title='Expand']", "xpath")){
			//TaskUtilities.jsFindThenClick("//div[text()='Define Implementation Users']"+"//a[@title='Expand']");
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Define Implementation Users']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Define Implementation Users']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Create Implementation Users']", MAX_TIME_OUT);
		
		openAdminLink();
		return true;
	}
	
	public void openAdminLink() throws Exception{
		
		String adminUrl = driver.findElement(By.xpath("//div[text()='Create Implementation Users']/../..//a[@title='Go to Task']")).getAttribute("href");
		
		WebElement body = driver.findElement(By.cssSelector("body"));
		//String newTabAction = Keys.chord(Keys.COMMAND, "t");
		String newTabAction = Keys.chord(Keys.CONTROL, "t");
        body.sendKeys(newTabAction);
         
        //String chooseTab = Keys.chord(Keys.COMMAND, "3");//on my pc 3 others should be 2;
		String switchTab = Keys.chord(Keys.CONTROL, Keys.TAB);
        body.sendKeys(switchTab);
        
        driver.get(adminUrl);        
		
		OracleIdentityManagerPage userPage = new OracleIdentityManagerPage(driver);
		takeScreenshot();
		
		userPage.clickAdminLink();
		TaskUtilities.customWaitForElementVisibility("//a[text()='Administration']", MAX_TIME_OUT);
		
		TaskUtilities.jsFindThenClick("//a[text()='Create User']");
		
		//After this is done to go back;;
		String closeTab = Keys.chord(Keys.CONTROL, "w");
		body.sendKeys(closeTab);
		
	}
	
}
