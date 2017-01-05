<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="me.bantling.j2ee.basics.view.bean.Country"%>
<%@ page import="me.bantling.j2ee.basics.view.bean.Region"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Address</title>
    <style type="text/css">
      .readonly input[type="text"] {
        /* Hide border, don't remove it - switching to edit doesn't change height */
        border-color: transparent;
        
        /* Show arrow mouse pointer instead of I beam, just like for plain text */
        cursor: default;
      }
      
      .readonly select {
        /* Hide border */
        border-color: transparent;
        
        /* On some OSes, the background color of a select is grey */
        background-color: transparent;
        
        /* Remove drop down arrow in non-IE browsers */
        -moz-appearance: none;
        -webkit-appearance: none;
        appearance: none;
        
        /* Show arrow mouse pointer */
        cursor: default;
      }
      
      .readonly select::-ms-expand {
        /* Remove drop down arrow in IE only */
        display: none;
      }
      
      select {
        /*
         * Specify the height a select should have, whether readonloy or editable.
         * The style changes above cause the height to change a bit if we don't.
         * 1.5em is fine for IE, but FF/Chrome need it slightly larger.
         * 1.75 em works in all.
         */ 
        height: 1.75em;
      }
      
      #addressForm th {
        /* By default, th has centered text, we want our labels left aligned */
        text-align: left;
        
        /*
         * Set width to the width needed for the longer Canadian labels.
         * When switching to USAand displaying the narrow labels, the columns remain the same size.
         */
        width: 5.5em;
      }
    </style>
    <script type="text/javascript">
      // ==== DOM element references
      
      var pageLoaded;
      
      (function() {
        var regionLabel;
        var postalCodeLabel;
        var countrySelect;
        var regionSelect;
        var editButton;
        var saveButton;
        
        // ==== Data structures
        
        var labelsByCountry = {};
        var regionsByCountry = {};
        
        // ==== Event handlers
        
        function readOnlyTextInputFocus(e) {
          // Remove focus so that the flashing I cursor disappears
          this.blur();
        }
        
        function readonlySelectMouseDown(e) {
          // Suppress normal mouse handling so the select doesn't drop down
          e.preventDefault();
          this.blur();
          window.focus();
        }
        
        function countrySelectChange(e) {
          // Remove all but "Select a Region" from regions drop down 
          regionSelect.options.length = 1;
          
          // Populate if the country isn't "Select a Country"
          if (countrySelect.selectedIndex !== 0) {
            // Get selected country code
            var selectedCountry = countrySelect.options[countrySelect.selectedIndex].value;
            
            // Set language of labels
            countryLabels = labelsByCountry[selectedCountry];
            regionLabel.innerText = countryLabels.regionLabel;
            postalCodeLabel.innerText = countryLabels.postalCodeLabel;
            regionSelect.options[0].text = "Select a " + countryLabels.regionLabel;
            
            // Populate regions of the selected country
            var countryRegions = regionsByCountry[selectedCountry];
            for (var i in countryRegions) {
              var region = countryRegions[i];
              
              regionSelect.options[regionSelect.options.length] = new Option(region.text, region.value);
            }
          }
        }
        
        function editClicked(e) {
          setViewEditable(true);
        }
        
        function saveClicked(e) {
          //
        }
        
        // ==== View state functions
        
        function setViewEditable(editable) {
          if (! editable) {
            document.body.classList.add("readonly");
            
            var textInputs = document.querySelectorAll("input[type='text']");
            for (var i = 0; i < textInputs.length; i++) {
              textInputs[i].setAttribute("readonly", "readonly");
              textInputs[i].addEventListener("focus", readOnlyTextInputFocus);
            }
            
            var selects = document.querySelectorAll("select");
            for (var i = 0; i < selects.length; i++) {
              selects[i].addEventListener("mousedown", readonlySelectMouseDown);
            }
            
            editButton.removeAttribute("disabled");
            saveButton.setAttribute("disabled", "disabled");
          } else {
            document.body.classList.remove("readonly");
            
            var textInputs = document.querySelectorAll("input[type='text']");
            for (var i = 0; i < textInputs.length; i++) {
              textInputs[i].removeAttribute("readonly");
              textInputs[i].removeEventListener("focus", readOnlyTextInputFocus);
            }
            
            var selects = document.querySelectorAll("select");
            for (var i = 0; i < selects.length; i++) {
              selects[i].removeEventListener("mousedown", readonlySelectMouseDown);
            }
            
            editButton.setAttribute("disabled", "disabled");
            saveButton.removeAttribute("disabled");
          }
        }
        
        // ==== Page load handler
        
        pageLoaded = function() {
        
          // ==== Initialize data structures
        
          // Create a JS mapping of all countries to their regions
          <c:forEach var="country" items="${Country.values()}">
            labelsByCountry["${country}"] = {
              regionLabel: "${country.regionLabel()}",
              postalCodeLabel: "${country.postalCodeLabel()}"
            }; 
          
            var countryRegions;
            regionsByCountry["${country}"] = countryRegions = [];
          
            <c:forEach var="region" items="${Region.values()}">
              <c:if test="${region.country == country}">
                countryRegions.push({value: "${region}", text: "${region.toString()}"});
              </c:if>
            </c:forEach>
          </c:forEach>
          
          // ==== Initialize DOM element references
          
          // Get the province and postal code labels
          regionLabel = document.querySelectorAll("label[for='region']")[0];
          postalCodeLabel = document.querySelectorAll("label[for='postalCode']")[0];
          
          // Get the country and region drop downs
          countrySelect = document.getElementById("country");
          regionSelect = document.getElementById("region");
          
          // Get the edit and save buttons
          editButton = document.getElementById("edit");
          saveButton = document.getElementById("save");
          
          // ==== Initialize event handlers
          
          countrySelect.addEventListener("change", countrySelectChange);
          editButton.addEventListener("click", editClicked);
          saveButton.addEventListener("click", saveClicked);
        
          // ==== Initialize view state
          
          setViewEditable(false);
        }
      })();
    </script>
  </head>
  <body onload="pageLoaded();">
    <h1>Address</h1>
    
    <c:if test="${not empty error}">
      <c:choose>
        <c:when test="${empty error.validationErrors}">
          <%-- We have an error, but it is not a validation error --%>
          <table id="error">
            <thead>
              <tr>
                <th>Code</th>
                <th>SubCode</th>
                <th>Message</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>${error.exceptionCode}</td>
                <td>${error.exceptionSubCode}</td>
                <td>${error.message}</td>
              </tr>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <%-- We have validation error(s) --%>
          <h2>Errors</h2>
          <table id="validation-errors">
            <thead>
              <tr>
                <th>Model Name</th>
                <th>Property</th>
                <th>Message</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="error" items="${error.validationErrors}">
                <tr>
                  <td>${error.modelName}</td>
                  <td>${error.property}</td>
                  <td>${error.message}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </c:if>
    
    <form id="addressForm" action="${pageContext.request.contextPath}${requestScope['javax.servlet.forward.servlet_path']}" method="post">
      <input type="hidden" name="id" value="${address.id}"/>
    
      <table>
        <tbody>
          <tr>
            <th><label for="line1">Line 1</label></th>
            <td><input type="text" id="line1" name="line1" value="${address.line1}"/></td>
          </tr><tr>
            <th><label for="line2">Line 2</label></th>
            <td><input type="text" id="line2" name="line2" value="${address.line2}"/></td>
          </tr><tr>
            <th><label for="city">City</label></th>
            <td><input type="text" id="city" name="city" value="${address.city}"/></td>
          </tr><tr>
            <th><label for="country">Country</label></th>
            <td>
              <select id="country" name="country">
                <option>Select a Country</option>
                <c:forEach var="country" items="${Country.values()}">
                  <option value="${country}"
                    <c:if test="${address.country == country}">selected="selected"</c:if>
                  >${country.toString()}</option>
                </c:forEach>
              </select>
            </td>
          </tr><tr>
            <th>
              <label for="region">${address.country.regionLabel()}</label>
            </th>
            <td>
              <select id="region" name="region">
                <option>Select a ${address.country.regionLabel()}</option>
                <c:forEach var="region" items="${Region.values()}">
                  <c:if test="${address.country == region.country}">
                    <option value="${region}"
                      <c:if test="${address.region == region}">selected="selected"</c:if>
                    >${region.toString()}</option>
                  </c:if>
                </c:forEach>
              </select>
            </td>
          </tr><tr>
            <th>
              <label for="postalCode">${address.country.postalCodeLabel()}</label>
            </th>
            <td><input type="text" id="postalCode" name="postalCode" value="${address.postalCode}"/></td>
          </tr>
        </tbody>
      </table>
      
      <table>
        <tbody>
          <tr>
            <td><button id="edit" type="button">Edit</button></td>
            <td><button id="save" type="submit">Save</button></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>
