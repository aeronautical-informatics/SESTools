<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:variable name="x"/>
    <xsl:template match="/">             
        <html> 
            <body>                
                <table border="0">    
                    <tr>
                        <td> 
                            <objects name="" text ="">
                                <xsl:text>&#xa;</xsl:text>
                                <object class="2">
                                    <description text=""/>
                                        <properties>
                                            <xsl:for-each select="//entity/aspect/entity[not(@name='WaypointParameters') and not(@name='PlannedState')]/var">
                                                <tr>
                                                    <td>                           
                                                        <xsl:variable name="vTransfers" select="@name"/>
                                                        <xsl:variable name="varDefault" select="@default"/>
                                                        
                                                        <xsl:choose>
                                                            <xsl:when test="$vTransfers[1] = 'rho'">                                                                
                                                                    <property pid="1" type="1" size="1" unit="kg/m3">
                                                                        <value value="{$varDefault}" />                                             
                                                                    </property>                                                                                                                                 
                                                            </xsl:when>
                                                            
                                                            <xsl:when test="$vTransfers[1] = 'a'">
                                                                <property pid="2" type="1" size="1" unit="kg/m3">
                                                                    <value value="{$varDefault}" />  
                                                                </property>
                                                            </xsl:when>
                                                            
                                                            <xsl:when test="$vTransfers[1] = 'T'">
                                                                <property pid="3" type="1" size="1" unit="kg/m3">
                                                                    <value value="{$varDefault}" />  
                                                                </property>
                                                            </xsl:when>
                                                            
                                                            <xsl:otherwise>
                                                                <property pid="3" type="1" size="1" unit="kg/m3">
                                                                    <value value="{$varDefault}" />  
                                                                </property>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </td>                            
                                                </tr>
                                            </xsl:for-each>
                                        </properties>                                    
                                    <xsl:text>&#xa;</xsl:text>
                                </object>
                                <xsl:text>&#xa;</xsl:text>                             
                            </objects>
                        </td>
                        
                        <td> 
                            <xsl:text>&#xa;</xsl:text>
                            <waypoints>
                                <xsl:text>&#xa;</xsl:text>
                                <waypoint>
                                    
                                    <properties>
                                        <xsl:for-each select="//entity/aspect/entity[(@name='WaypointParameters')]/var">
                                            <tr>
                                                <td>                           
                                                    <xsl:variable name="vTransfers" select="@name"/>
                                                    <xsl:variable name="varDefault" select="@default"/>
                                                    
                                                    <xsl:choose>
                                                        <xsl:when test="$vTransfers[1] = 'rho'">                                                                
                                                            <property pid="1" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />                                             
                                                            </property>                                                                                                                                 
                                                        </xsl:when>
                                                        
                                                        <xsl:when test="$vTransfers[1] = 'a'">
                                                            <property pid="2" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />  
                                                            </property>
                                                        </xsl:when>
                                                        
                                                        <xsl:when test="$vTransfers[1] = 'T'">
                                                            <property pid="3" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />  
                                                            </property>
                                                        </xsl:when>
                                                        
                                                        <xsl:otherwise>
                                                            <property pid="3" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />  
                                                            </property>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </td>                            
                                            </tr>
                                        </xsl:for-each>
                                        
                                        <xsl:for-each select="//entity/aspect/entity[(@name='PlannedState')]/var">
                                            <tr>
                                                <td>                           
                                                    <xsl:variable name="vTransfers" select="@name"/>
                                                    <xsl:variable name="varDefault" select="@default"/>
                                                    
                                                    <xsl:choose>
                                                        <xsl:when test="$vTransfers[1] = 'rho'">                                                                
                                                            <property pid="1" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />                                             
                                                            </property>                                                                                                                                 
                                                        </xsl:when>
                                                        
                                                        <xsl:when test="$vTransfers[1] = 'a'">
                                                            <property pid="2" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />  
                                                            </property>
                                                        </xsl:when>
                                                        
                                                        <xsl:when test="$vTransfers[1] = 'T'">
                                                            <property pid="3" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />  
                                                            </property>
                                                        </xsl:when>
                                                        
                                                        <xsl:otherwise>
                                                            <property pid="3" type="1" size="1" unit="kg/m3">
                                                                <value value="{$varDefault}" />  
                                                            </property>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </td>                            
                                            </tr>
                                        </xsl:for-each>
                                    </properties>                                    
                                    <xsl:text>&#xa;</xsl:text>
                                </waypoint>
                                <xsl:text>&#xa;</xsl:text>                             
                            </waypoints>
                        </td>
                    </tr>
                    
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

