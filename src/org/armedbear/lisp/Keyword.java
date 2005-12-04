/*
 * Keyword.java
 *
 * Copyright (C) 2002-2005 Peter Graves
 * $Id: Keyword.java,v 1.48 2005-12-04 13:34:49 piso Exp $
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.armedbear.lisp;

public final class Keyword extends Lisp
{
    public static final Symbol
        ABCL                = internKeyword("ABCL"),
        ABORT               = internKeyword("ABORT"),
        ABSOLUTE            = internKeyword("ABSOLUTE"),
        ADJUSTABLE          = internKeyword("ADJUSTABLE"),
        ALLOW_OTHER_KEYS    = internKeyword("ALLOW-OTHER-KEYS"),
        ANSI_CL             = internKeyword("ANSI-CL"),
        APPEND              = internKeyword("APPEND"),
        ARMEDBEAR           = internKeyword("ARMEDBEAR"),
        BACK                = internKeyword("BACK"),
        BOOLEAN             = internKeyword("BOOLEAN"),
        CAPITALIZE          = internKeyword("CAPITALIZE"),
        CAPITALIZE_FIRST    = internKeyword("CAPITALIZE-FIRST"),
        CASE                = internKeyword("CASE"),
        CAUSE               = internKeyword("CAUSE"),
        CHAR                = internKeyword("CHAR"),
        COMMON              = internKeyword("COMMON"),
        COMMON_LISP         = internKeyword("COMMON-LISP"),
        COMPILE_TOPLEVEL    = internKeyword("COMPILE-TOPLEVEL"),
        COUNT_ONLY          = internKeyword("COUNT-ONLY"),
        CREATE              = internKeyword("CREATE"),
        DARWIN              = internKeyword("DARWIN"),
        DATUM               = internKeyword("DATUM"),
        DECLARED            = internKeyword("DECLARED"),
        DEFAULT             = internKeyword("DEFAULT"),
        DEFAULTS            = internKeyword("DEFAULTS"),
        DEVICE              = internKeyword("DEVICE"),
        DIRECTION           = internKeyword("DIRECTION"),
        DIRECTORY           = internKeyword("DIRECTORY"),
        DIRECT_SUPERCLASSES = internKeyword("DIRECT-SUPERCLASSES"),
        DOWNCASE            = internKeyword("DOWNCASE"),
        ELEMENT_TYPE        = internKeyword("ELEMENT-TYPE"),
        END                 = internKeyword("END"),
        ERROR               = internKeyword("ERROR"),
        EXECUTE             = internKeyword("EXECUTE"),
        EXPECTED_TYPE       = internKeyword("EXPECTED-TYPE"),
        EXTERNAL            = internKeyword("EXTERNAL"),
        EXTERNAL_FORMAT     = internKeyword("EXTERNAL-FORMAT"),
        FILL_POINTER        = internKeyword("FILL-POINTER"),
        FORMAT_ARGUMENTS    = internKeyword("FORMAT-ARGUMENTS"),
        FORMAT_CONTROL      = internKeyword("FORMAT-CONTROL"),
        FROM_END            = internKeyword("FROM-END"),
        HOST                = internKeyword("HOST"),
        IF_DOES_NOT_EXIST   = internKeyword("IF-DOES-NOT-EXIST"),
        IF_EXISTS           = internKeyword("IF-EXISTS"),
        INHERITED           = internKeyword("INHERITED"),
        INITIAL_CONTENTS    = internKeyword("INITIAL-CONTENTS"),
        INITIAL_ELEMENT     = internKeyword("INITIAL-ELEMENT"),
        INPUT               = internKeyword("INPUT"),
        INSTANCE            = internKeyword("INSTANCE"),
        INT                 = internKeyword("INT"),
        INTERNAL            = internKeyword("INTERNAL"),
        INVERT              = internKeyword("INVERT"),
        IO                  = internKeyword("IO"),
        J                   = internKeyword("J"),
        JAVA_1_4            = internKeyword("JAVA-1.4"),
        JAVA_1_5            = internKeyword("JAVA-1.5"),
        JAVA_1_6            = internKeyword("JAVA-1.6"),
        KEY                 = internKeyword("KEY"),
        LINUX               = internKeyword("LINUX"),
        LOAD_TOPLEVEL       = internKeyword("LOAD-TOPLEVEL"),
        LOCAL               = internKeyword("LOCAL"),
        NAME                = internKeyword("NAME"),
        NEW_VERSION         = internKeyword("NEW"),
        NEWEST              = internKeyword("NEWEST"),
        NICKNAMES           = internKeyword("NICKNAMES"),
        NONE                = internKeyword("NONE"),
        NO_ERROR            = internKeyword("NO-ERROR"),
        OBJECT              = internKeyword("OBJECT"),
        OPERANDS            = internKeyword("OPERANDS"),
        OPERATION           = internKeyword("OPERATION"),
        OUTPUT              = internKeyword("OUTPUT"),
        OVERFLOW            = internKeyword("OVERFLOW"),
        OVERWRITE           = internKeyword("OVERWRITE"),
        PACKAGE             = internKeyword("PACKAGE"),
        PATHNAME            = internKeyword("PATHNAME"),
        PROBE               = internKeyword("PROBE"),
        PUBLIC              = internKeyword("PUBLIC"),
        PRESERVE            = internKeyword("PRESERVE"),
        REF                 = internKeyword("REF"),
        RELATIVE            = internKeyword("RELATIVE"),
        RENAME              = internKeyword("RENAME"),
        RENAME_AND_DELETE   = internKeyword("RENAME-AND-DELETE"),
        SIZE                = internKeyword("SIZE"),
        START               = internKeyword("START"),
        STATUS              = internKeyword("STATUS"),
        STREAM              = internKeyword("STREAM"),
        SUNOS               = internKeyword("SUNOS"),
        SUPERSEDE           = internKeyword("SUPERSEDE"),
        TEST                = internKeyword("TEST"),
        TEST_NOT            = internKeyword("TEST-NOT"),
        TIME                = internKeyword("TIME"),
        TOP_LEVEL           = internKeyword("TOP-LEVEL"),
        TRAPS               = internKeyword("TRAPS"),
        TYPE                = internKeyword("TYPE"),
        UNDERFLOW           = internKeyword("UNDERFLOW"),
        UNIX                = internKeyword("UNIX"),
        UNSPECIFIC          = internKeyword("UNSPECIFIC"),
        UP                  = internKeyword("UP"),
        UPCASE              = internKeyword("UPCASE"),
        USE                 = internKeyword("USE"),
        VERSION             = internKeyword("VERSION"),
        WILD                = internKeyword("WILD"),
        WILD_INFERIORS      = internKeyword("WILD-INFERIORS"),
        WINDOWS             = internKeyword("WINDOWS");
}
