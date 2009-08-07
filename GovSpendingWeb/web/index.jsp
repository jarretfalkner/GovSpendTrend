<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : index
    Created on : Jun 9, 2009, 1:38:22 PM
    Author     : Jarret
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="govspending.*" %>
<%@page import="govspending.listeners.*" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.*" %>
<%
ContractsManager dl = (ContractsManager)getServletContext().getAttribute(DataLoaderApplicationListener.DATA_LOADER_CONTEXT_KEY);
request.setAttribute("numLoaded", Integer.toString(dl.getTotalLoaded()));

String query = request.getParameter("q");
if(query != null && StringUtils.isNotBlank(query)){
    List<YearQueryResult> res = dl.queryFor(query);
    request.setAttribute("imgUrl", dl.googleChartFor(query));
    request.setAttribute("q", query);

    String details = request.getParameter("details");
    if(details != null){
        request.setAttribute("details", res);
    }
}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SpendTrend.us - Easily search government spending trends</title>
	<style type="text/css" media="screen">
	<!--
	body
	{
		padding: 0;
		margin: 0;
		background-color: #666;
		color: #000;
		text-align: center;
	}

	#contents
	{
		margin-top: 10px;
		margin-bottom: 10px;
		margin-right:auto;
		margin-left:auto;
		width: 900px;
		padding: 10px;
		background-color: #FFF;
		color: #000;
		text-align: left;
	}

	h1
	{
		color: #333;
		background-color: transparent;
		font-family: Arial, Helvetica, sans-serif;
		font-size: 20px;
	}

	h2
	{
		color: #333;
		background-color: transparent;
		font-family: Arial, Helvetica, sans-serif;
		font-size: 18px;
	}

	h3
	{
		color: #333;
		background-color: transparent;
		font-family: Arial, Helvetica, sans-serif;
		font-size: 16px;
        padding-top: 4px;
	}
	-->
</style>

    </head>
    <body>
        <div id="contents">
            <div style="padding-top: 0px; padding-left: 50px; padding-bottom: 40px;">
                <center><img src="images/logo.png" /></center>
                <table>
                    <tr>
                        <td style="padding-right: 20px">
                            <span style="font-size: 200%">Search Spending:</span>
                        </td>
                        <td>
                            <form action="index.jsp" method="get">
                                <input type="text" size="50" name="q" value="${q}" style="padding: 5px; font-size: 120%;" />
                                <input type="submit" name="search" value="search" style="padding: 5px; font-size: 120%;" /><br/>
                                <span style="font-size: smaller">Try <a href="index.jsp?q=science+healthcare">science, healthcare</a> or <a href="index.jsp?q=oil+solar+wind">oil, solar, and wind</a> or <a href="index.jsp?q=recovery+katrina">recovery, katrina</a> or <a href="index.jsp?q=israel+iraq">israel, iraq</a><br/></span>
                            </form>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="padding-left: 20px; padding-right: 20px;">
                <c:if test="${imgUrl != null}">
                    <h3>Spending results for ${q}</h3>
                    <img src="${imgUrl}" /><br/>
                    <a href="index.jsp?q=${q}&details=on">Show details...</a>
                </c:if>
                <c:if test="${details != null}">
                        <c:forEach var="yearQueryResult" items="${details}">
                            <h3>${yearQueryResult.queryTerm} contracts in ${yearQueryResult.year}: $${yearQueryResult.matches.totalValue}M</h3>

                    <table>
                        <thead><th>Contract Value (in millions)</th><TH>Contractor</TH><th>Parent Contractor</th><th>Contract Description</th></thead>
                        <c:forEach var="contract" items="${yearQueryResult.matches.contracts}">
                                <tr><td>${contract.howMuch}</td><td>${contract.contractor}</td><td>${contract.parent}</td><td>${contract.descriptionOfContract}</td></tr>
                            </c:forEach>
                    </table>
                        </c:forEach>
                </c:if>

                <h3>How it works</h3>
                <a href="http://www.usaspending.gov/">USASpending.gov</a> publishes summaries of awarded federal contracts.  We <a href="http://en.wikipedia.org/wiki/Inverted_index">index</a> these contracts so you can search and visualize trends in government spending. Currently, the index contains ${numLoaded} government contracts starting in 2000.

                <h3>Apps for America 2 and Data.gov</h3>
                This application is an entry in <a href="http://sunlightlabs.com/contests/appsforamerica2/">Apps for America 2</a> using <a href="http://www.data.gov/details/132">Data.gov</a>. Specifically, we're aiming for three goals:
                <ul>
                    <li>Transparency - Allow everyone to search USASpending.gov data using a simple text box instead of <a href="http://www.fedspending.org/fpds/search.php">crammed</a> <a href="http://www.usaspending.gov/fpds/vendor.php">forms</a>. By making data easily searchable, citizens gain insight into federal government contract spending.</li>
                    <li>Permanence - USASpending.gov regularly improves data quality and adds new contracts. By indexing the data updates, this application remains fresh, usable, and relevant for contract spending research.</li>
                    <li>Design &amp; Visualization - Our visualization shows how much money is spent on each word in your query. Try searching for <a href="index.jsp?q=oil+solar+wind">oil, solar, and wind</a> to get a sense of where our government spends the energy budget.</li>
                </ul>
                <a href="http://github.com/jarretfalkner/GovSpendTrend/tree/master">GitHub</a> has the source code.

                <h3>Contact</h3>
                Reach <a href="http://jarret.falkfalk.com">me</a> at jar at cs dot washington dot edu.
            </div>
        </div>
    </body>
</html>
