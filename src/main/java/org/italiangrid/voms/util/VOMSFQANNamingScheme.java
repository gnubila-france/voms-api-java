/*********************************************************************
 *
 * Authors: 
 *      Karoly Lorentey - lorentey@elte.hu
 *      Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it 
 *          
 * Copyright (c) Members of the EGEE Collaboration. 2004-2010.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Parts of this code may be based upon or even include verbatim pieces,
 * originally written by other people, in which case the original header
 * follows.
 *
 *********************************************************************/
package org.italiangrid.voms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.italiangrid.voms.VOMSError;

/**
 * This class provides utility methods that are used for parsing, matching voms
 * FQANs (Fully Qualified Attribute Names).
 * 
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti </a>
 * 
 * 
 */
public class VOMSFQANNamingScheme {

	public static final String containerSyntax = "^(/[\\w.-]+)+|((/[\\w.-]+)+/)?(Role=[\\w.-]+)|(Capability=[\\w\\s.-]+)$";

	public static final String groupSyntax = "^(/[\\w.-]+)+$";

	public static final String roleSyntax = "^Role=[\\w.-]+$";

	public static final String qualifiedRoleSyntax = "^(/[\\w.-]+)+/Role=[\\w.-]+$";

	public static final String capabilitySyntax = "^Capability=[\\w\\s.-]+$";

	public static final Pattern containerPattern = Pattern
			.compile(containerSyntax);

	public static final Pattern groupPattern = Pattern.compile(groupSyntax);

	public static final Pattern rolePattern = Pattern.compile(roleSyntax);

	public static final Pattern qualifiedRolePattern = Pattern
			.compile(qualifiedRoleSyntax);

	public static final Pattern capabilityPattern = Pattern
			.compile(capabilitySyntax);

	/**
	 * This methods checks that the string passed as argument complies with the
	 * voms FQAN syntax.
	 * 
	 * @param containerName
	 *            the string that must be checked for compatibility with FQAN
	 *            syntax.
	 * @throws VOMSError
	 *             If there's an error in the FQAN syntax.
	 */
	public static void checkSyntax(String containerName) {

		if (containerName.length() > 255)
			throw new VOMSError("containerName.length() > 255");

		if (!containerPattern.matcher(containerName).matches())
			throw new VOMSError("Syntax error in container name: "
					+ containerName);
	}

	/**
	 * 
	 * This methods checks that the string passed as argument complies with the
	 * syntax used by voms to identify groups.
	 * 
	 * @param groupName
	 *            the string that has to be checked.
	 * @throws VOMSError
	 *             If the string passed as argument doens not comply with the
	 *             voms sytax.
	 */
	public static void checkGroup(String groupName) {

		checkSyntax(groupName);

		if (!groupPattern.matcher(groupName).matches())
			throw new VOMSError("Syntax error in group name: "
					+ groupName);
	}

	/**
	 * This methods checks that the string passed as argument complies with the
	 * syntax used by voms to identify roles.
	 * 
	 * 
	 * @param roleName
	 * @throws VOMSError
	 *             If the string passed as argument doens not comply with the
	 *             voms sytax.
	 */
	public static void checkRole(String roleName) {

		if (roleName.length() > 255)
			throw new VOMSError("roleName.length()>255");

		if (!rolePattern.matcher(roleName).matches())
			throw new VOMSError("Syntax error in role name: "
					+ roleName);
	}

	/**
	 * This methods checks that the FQAN passed as argument identifies a voms
	 * group.
	 * 
	 * @param groupName
	 *            the string to check.
	 * @return <ul>
	 *         <li>true, if the string passed as argument identifies a voms
	 *         group.
	 *         <li>false, otherwise.
	 *         </ul>
	 */
	public static boolean isGroup(String groupName) {

		checkSyntax(groupName);

		return groupPattern.matcher(groupName).matches();
	}

	/**
	 * This methods checks that the FQAN passed as argument identifies a voms
	 * role.
	 * 
	 * @param roleName
	 *            the string to check.
	 * @return <ul>
	 *         <li>true, if the string passed as argument identifies a voms
	 *         role.
	 *         <li>false, otherwise.
	 *         </ul>
	 */
	public static boolean isRole(String roleName) {

		checkSyntax(roleName);
		return rolePattern.matcher(roleName).matches();
	}

	/**
	 * This methods checks that the FQAN passed as argument identifies a
	 * qualified voms role, i.e., a role defined in the context of a voms group.
	 * 
	 * @param roleName
	 *            the string to check.
	 * @return <ul>
	 *         <li>true, if the string passed as argument identifies a qualified
	 *         voms role.
	 *         <li>false, otherwise.
	 *         </ul>
	 */
	public static boolean isQualifiedRole(String roleName) {

		checkSyntax(roleName);
		return qualifiedRolePattern.matcher(roleName).matches();
	}

	/**
	 * This method extracts the role name information from the FQAN passed as
	 * argument.
	 * 
	 * @param containerName
	 *            the FQAN
	 * @return <ul>
	 *         <li>A string containing the role name, if found</li>
	 *         <li>null, if no role information is contained in the FQAN passed
	 *         as argument
	 *         </ul>
	 */
	public static String getRoleName(String containerName) {

		if (!isRole(containerName) && !isQualifiedRole(containerName))
			throw new VOMSError("No role specified in \""
					+ containerName + "\" voms syntax.");

		Matcher m = containerPattern.matcher(containerName);

		if (m.matches()) {

			String roleGroup = m.group(4);
			return roleGroup.substring(roleGroup.indexOf('=') + 1,
					roleGroup.length());

		}

		return null;
	}

	/**
	 * This method extracts group name information from the FQAN passed as
	 * argument.
	 * 
	 * @param containerName
	 *            the FQAN
	 * @return <ul>
	 *         <li>A string containing the group name, if found</li>
	 *         <li>null, if no group information is contained in the FQAN passed
	 *         as argument
	 *         </ul>
	 */
	public static String getGroupName(String containerName) {

		checkSyntax(containerName);

		// If it's a container and it's not a role or a qualified role, then
		// it's a group!

		if (!isRole(containerName) && !isQualifiedRole(containerName))
			return containerName;

		Matcher m = containerPattern.matcher(containerName);

		if (m.matches()) {
			String groupName = m.group(2);

			if (groupName.endsWith("/"))
				return groupName.substring(0, groupName.length() - 1);
			else
				return groupName;
		}

		return null;
	}

	public static String toOldQualifiedRoleSyntax(String qualifiedRole) {

		checkSyntax(qualifiedRole);

		if (!isQualifiedRole(qualifiedRole))
			throw new VOMSError(
					"String passed as argument is not a qualified role!");

		return getGroupName(qualifiedRole) + ":" + getRoleName(qualifiedRole);

	}
}
