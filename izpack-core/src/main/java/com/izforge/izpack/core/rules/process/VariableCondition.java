/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2007-2009 Dennis Reil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.core.rules.process;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.adaptator.impl.XMLElementImpl;
import com.izforge.izpack.api.rules.Condition;
import com.izforge.izpack.api.substitutor.VariableSubstitutor;
import com.izforge.izpack.core.substitutor.VariableSubstitutorImpl;
import com.izforge.izpack.util.Debug;

/**
 * @author Dennis Reil, <izpack@reil-online.de>
 */
public class VariableCondition extends Condition
{

    /**
     *
     */
    private static final long serialVersionUID = 2881336115632480575L;

    protected String variablename;

    protected String value;

//    public VariableCondition(String variablename, String value, AutomatedInstallData installdata)
//    {
//        super(installdata);
//        this.variablename = variablename;
//        this.value = value;
//    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getVariablename()
    {
        return variablename;
    }

    public void setVariablename(String variablename)
    {
        this.variablename = variablename;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.reddot.installer.rules.Condition#readFromXML(com.izforge.izpack.api.adaptator.IXMLElement)
     */

    public void readFromXML(IXMLElement xmlcondition)
    {
        try
        {
            this.variablename = xmlcondition.getFirstChildNamed("name").getContent();
            this.value = xmlcondition.getFirstChildNamed("value").getContent();
        }
        catch (Exception e)
        {
            Debug.log("missing element in <condition type=\"variable\"/>");
        }

    }

    public boolean isTrue()
    {
        if (this.getInstalldata() != null)
        {
            String val = this.getInstalldata().getVariable(variablename);
            if (val == null)
            {
                return false;
            }
            else
            {
                VariableSubstitutor subst = new VariableSubstitutorImpl(this.getInstalldata().getVariables());
                return val.equals(subst.substitute(value));
            }
        }
        else
        {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.izforge.izpack.api.rules.Condition#getDependenciesDetails()
     */

    public String getDependenciesDetails()
    {
        StringBuffer details = new StringBuffer();
        details.append(this.getId());
        details.append(" depends on a value of <b>");
        details.append(this.value);
        details.append("</b> on variable <b>");
        details.append(this.variablename);
        details.append(" (current value: ");
        details.append(this.getInstalldata().getVariable(variablename));
        details.append(")");
        details.append("</b><br/>");
        return details.toString();
    }

    @Override
    public void makeXMLData(IXMLElement conditionRoot)
    {
        XMLElementImpl nameEl = new XMLElementImpl("name", conditionRoot);
        nameEl.setContent(this.variablename);
        conditionRoot.addChild(nameEl);

        XMLElementImpl valueEl = new XMLElementImpl("value", conditionRoot);
        valueEl.setContent(this.value);
        conditionRoot.addChild(valueEl);
    }
}