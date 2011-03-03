<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<jsp:include page="jsp/layout/header_menu.jsp" />
      <div id="main">
        <!-- begin: #col1 - first float column -->
        <div id="col1" role="complementary">
          <div id="col1_content" class="clearfix">
          </div>
        </div><!-- end: #col1 -->
        <!-- begin: #col3 static column -->
        <div id="col3" role="main">
          <div id="col3_content" class="clearfix">
          <div class="header_section yform">
          <span class="name_section">Manage Guests</span>
          <div class="right type-text">
          <input type="text" name="guest_search" class="txt_guest_search" /><button class="btn_g_search">SEARCH</button>
            <div class="search_links"><span>Or browse:&nbsp;</span><a href="#">staying this week</a><span>,&nbsp;</span><a href="#">staying next week</a>,&nbsp;<a href="#">staying this month</a>,&nbsp;<a href="#">staying last month</a>,&nbsp;<a href="#">all</a></div>
            </div>
          </div>
          <div>
		 <form method="post" action="" class="yform full" role="application">
            <fieldset>
              <legend>Latest Guests</legend>
             <div class="subcolumns">
             <span>
</span>       </div>
			 <div class="subcolumns">
      		<div class="c25l">
                    <div class="type_rooms">
					<a href="guest_edit.jsp"><span class="title_season">Giovanni Rossi</span></a>
					<p></p>
					<ul>
					<li>34057546746</li>
					<li>rossi@tiscali.it</li>
					<li>Arriving: *****</li></ul>
                    </div>                  
                    </div>
                    </div>
               <div class="subcolumns">
              &nbsp;
              </div>
             <div class="subcolumns">
                   <div class="type_rooms">
                    </div> 
                    <div class="type-select num_of_rooms">
                    </div> 
              </div>
              <div class="subcolumns">
              &nbsp;
              </div>
            <div class="subcolumns type-text">
            <div class="c50l">
      		</div>
      		</div>
      		<div class="subcolumns">
              &nbsp;
              </div>
            <div class="type-button">
            <input type="text" name="new_name_season" id="chng_season_name" value=""/>
            <button class="btn_add_guest">Add New Guest</button>
            </div>
              </fieldset>
           </form>        
		</div>        
          </div>
          <jsp:include page="jsp/layout/footer.jsp" />   