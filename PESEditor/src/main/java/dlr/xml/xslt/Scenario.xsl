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
                                    <tr>                               
                                        
                                        <td> 
                                            
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
                                                                    <property pid="2" type="1" size="1" unit="m/s">
                                                                        <value value="{$varDefault}" />  
                                                                    </property>
                                                                </xsl:when>
                                                                
                                                                <xsl:when test="$vTransfers[1] = 'T'">
                                                                    <property pid="3" type="1" size="1" unit="K">
                                                                        <value value="{$varDefault}" />  
                                                                    </property>
                                                                </xsl:when>                                                            
                                                                
                                                                <xsl:when test="$vTransfers[1] = 'lat'">
                                                                    <property pid="50" type="1" size="1" unit="rad">
                                                                        <value value="{$varDefault}" />  
                                                                    </property>
                                                                </xsl:when>
                                                                
                                                                <xsl:when test="$vTransfers[1] = 'lon'">
                                                                    <property pid="51" type="1" size="1" unit="rad">
                                                                        <value value="{$varDefault}" />  
                                                                    </property>
                                                                </xsl:when>
                                                                
                                                            </xsl:choose>
                                                        </td>                            
                                                    </tr>
                                                </xsl:for-each>
                                            </properties>                                    
                                            <xsl:text>&#xa;</xsl:text>
                                            
                                        </td>
                                        
                                        <td> 
                                            <xsl:text>&#xa;</xsl:text>
                                            <waypoints>
                                                <xsl:text>&#xa;</xsl:text>
                                                
                                                <xsl:variable name="numWaypoints" select="count(//entity[@name='Waypoint_'])"/>
                                                <xsl:for-each select=".">
                                                    <xsl:for-each select="//entity/aspect/entity[(@name='WaypointParameters')]/var">
                                                        <xsl:variable name="enName" select="@name"/>                                       
                                                        <value value="{$enName}" />
                                                    </xsl:for-each>                                                    
                                                </xsl:for-each>
                                                
                                                
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
                                                                            <property pid="2" type="1" size="1" unit="m/s">
                                                                                <value value="{$varDefault}" />  
                                                                            </property>
                                                                        </xsl:when>
                                                                        
                                                                        <xsl:when test="$vTransfers[1] = 'T'">
                                                                            <property pid="3" type="1" size="1" unit="K">
                                                                                <value value="{$varDefault}" />  
                                                                            </property>
                                                                        </xsl:when> 
                                                                        
                                                                        <xsl:when test="$vTransfers[1] = 'lat'">
                                                                            <property pid="50" type="1" size="1" unit="rad">
                                                                                <value value="{$varDefault}" />  
                                                                            </property>
                                                                        </xsl:when>
                                                                        
                                                                        <xsl:when test="$vTransfers[1] = 'lon'">
                                                                            <property pid="51" type="1" size="1" unit="rad">
                                                                                <value value="{$varDefault}" />  
                                                                            </property>
                                                                        </xsl:when>
                                                                        
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
                                                                        <xsl:when test="$vTransfers[1] = 'Psi_c'">                                                                
                                                                            <property pid="54" type="1" size="1" unit="kg/m3">
                                                                                <value value="{$varDefault}" />                                             
                                                                            </property>                                                                                                                                 
                                                                        </xsl:when>                                                       
                                                                        
                                                                        
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
                                </object>
                                <xsl:text>&#xa;</xsl:text>                             
                            </objects>
                        </td>
                    </tr>
                    
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

