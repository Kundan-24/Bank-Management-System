console.log("Script loaded");
// ----Change theme work -----
let currentTheme = getTheme();
//----initial----

//-- run code after loading the page---
document.addEventListener('DOMContentLoaded', ()=>{
changeTheme();
});


//TODO
function changeTheme(){
  //set to web page
  changePageTheme(currentTheme, currentTheme === "light" ? "dark" : "light");
  //set the listener to change theme button
  const changeThemeButton = document.querySelector("#theme_change_button");
  changeThemeButton.addEventListener("click", (event) => {
      const oldTheme=currentTheme;
      currentTheme = currentTheme === "dark" ? "light" : "dark";
      changePageTheme(currentTheme, oldTheme)
      console.log("change theme button clicked"); 
  });
}


//set theme to localstorage
function setTheme(theme){
    localStorage.setItem("theme",theme);
}

//get theme from localstorage
function getTheme(){
    return localStorage.getItem("theme") || "light"; 
}

//change current page theme
function changePageTheme(theme, oldTheme){
     //localstorage me update karege
    setTheme(theme);
    const html = document.querySelector("html");
    //remove the current theme
    html.classList.remove(oldTheme);
    //set the current Theme
    html.classList.add(theme);

    //change the text of button
   document.querySelector("#theme_change_button span").textContent = theme === "light" ? "Dark" : "Light";
}

// ----End of Change theme work -----