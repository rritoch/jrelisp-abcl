;;; opcodes.lisp
;;;
;;; Copyright (C) 2003-2004 Peter Graves
;;; $Id: opcodes.lisp,v 1.6 2004-07-11 16:00:22 piso Exp $
;;;
;;; This program is free software; you can redistribute it and/or
;;; modify it under the terms of the GNU General Public License
;;; as published by the Free Software Foundation; either version 2
;;; of the License, or (at your option) any later version.
;;;
;;; This program is distributed in the hope that it will be useful,
;;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;;; GNU General Public License for more details.
;;;
;;; You should have received a copy of the GNU General Public License
;;; along with this program; if not, write to the Free Software
;;; Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

(in-package "JVM")

(defparameter *opcodes* (make-hash-table :test 'equalp))

(defstruct jvm-opcode name number size stack-effect)

(defmacro define-opcode (name number size stack-effect)
  `(let* ((name (string ',name))
          (opcode (make-jvm-opcode :name name
                                   :number ,number
                                   :size ,size
                                   :stack-effect ,stack-effect)))
     (setf (gethash name *opcodes*) opcode)
     (setf (gethash ,number *opcodes*) opcode)))

;; name number size stack-effect (nil if unknown)
(define-opcode nop 0 1 nil)
(define-opcode aconst_null 1 1 1)
(define-opcode iconst_m1 2 1 1)
(define-opcode iconst_0 3 1 1)
(define-opcode iconst_1 4 1 1)
(define-opcode iconst_2 5 1 1)
(define-opcode iconst_3 6 1 1)
(define-opcode iconst_4 7 1 1)
(define-opcode iconst_5 8 1 1)
(define-opcode lconst_0 9 1 nil)
(define-opcode lconst_1 10 1 nil)
(define-opcode fconst_0 11 1 nil)
(define-opcode fconst_1 12 1 nil)
(define-opcode fconst_2 13 1 nil)
(define-opcode dconst_0 14 1 nil)
(define-opcode dconst_1 15 1 nil)
(define-opcode bipush 16 2 1)
(define-opcode sipush 17 3 1)
(define-opcode ldc 18 2 1)
(define-opcode ldc_w 19 3 1)
(define-opcode ldc2_w 20 3 nil)
(define-opcode iload 21 2 nil)
(define-opcode lload 22 2 nil)
(define-opcode fload 23 2 nil)
(define-opcode dload 24 2 nil)
(define-opcode aload 25 2 1)
(define-opcode iload_0 26 1 nil)
(define-opcode iload_1 27 1 nil)
(define-opcode iload_2 28 1 nil)
(define-opcode iload_3 29 1 nil)
(define-opcode lload_0 30 1 nil)
(define-opcode lload_1 31 1 nil)
(define-opcode lload_2 32 1 nil)
(define-opcode lload_3 33 1 nil)
(define-opcode fload_0 34 1 nil)
(define-opcode fload_1 35 1 nil)
(define-opcode fload_2 36 1 nil)
(define-opcode fload_3 37 1 nil)
(define-opcode dload_0 38 1 nil)
(define-opcode dload_1 39 1 nil)
(define-opcode dload_2 40 1 nil)
(define-opcode dload_3 41 1 nil)
(define-opcode aload_0 42 1 1)
(define-opcode aload_1 43 1 1)
(define-opcode aload_2 44 1 1)
(define-opcode aload_3 45 1 1)
(define-opcode iaload 46 1 nil)
(define-opcode laload 47 1 nil)
(define-opcode faload 48 1 nil)
(define-opcode daload 49 1 nil)
(define-opcode aaload 50 1 -1)
(define-opcode baload 51 1 nil)
(define-opcode caload 52 1 nil)
(define-opcode saload 53 1 nil)
(define-opcode istore 54 2 nil)
(define-opcode lstore 55 2 nil)
(define-opcode fstore 56 2 nil)
(define-opcode dstore 57 2 nil)
(define-opcode astore 58 2 -1)
(define-opcode istore_0 59 1 nil)
(define-opcode istore_1 60 1 nil)
(define-opcode istore_2 61 1 nil)
(define-opcode istore_3 62 1 nil)
(define-opcode lstore_0 63 1 nil)
(define-opcode lstore_1 64 1 nil)
(define-opcode lstore_2 65 1 nil)
(define-opcode lstore_3 66 1 nil)
(define-opcode fstore_0 67 1 nil)
(define-opcode fstore_1 68 1 nil)
(define-opcode fstore_2 69 1 nil)
(define-opcode fstore_3 70 1 nil)
(define-opcode dstore_0 71 1 nil)
(define-opcode dstore_1 72 1 nil)
(define-opcode dstore_2 73 1 nil)
(define-opcode dstore_3 74 1 nil)
(define-opcode astore_0 75 1 -1)
(define-opcode astore_1 76 1 -1)
(define-opcode astore_2 77 1 -1)
(define-opcode astore_3 78 1 -1)
(define-opcode iastore 79 1 nil)
(define-opcode lastore 80 1 nil)
(define-opcode fastore 81 1 nil)
(define-opcode dastore 82 1 nil)
(define-opcode aastore 83 1 -3)
(define-opcode bastore 84 1 nil)
(define-opcode castore 85 1 nil)
(define-opcode sastore 86 1 nil)
(define-opcode pop 87 1 -1)
(define-opcode pop2 88 1 nil)
(define-opcode dup 89 1 1)
(define-opcode dup_x1 90 1 1)
(define-opcode dup_x2 91 1 1)
(define-opcode dup2 92 1 nil)
(define-opcode dup2_x1 93 1 nil)
(define-opcode dup2_x2 94 1 nil)
(define-opcode swap 95 1 0)
(define-opcode iadd 96 1 nil)
(define-opcode ladd 97 1 nil)
(define-opcode fadd 98 1 nil)
(define-opcode dadd 99 1 nil)
(define-opcode isub 100 1 nil)
(define-opcode lsub 101 1 nil)
(define-opcode fsub 102 1 nil)
(define-opcode dsub 103 1 nil)
(define-opcode imul 104 1 nil)
(define-opcode lmul 105 1 nil)
(define-opcode fmul 106 1 nil)
(define-opcode dmul 107 1 nil)
(define-opcode idiv 108 1 nil)
(define-opcode ldiv 109 1 nil)
(define-opcode fdiv 110 1 nil)
(define-opcode ddiv 111 1 nil)
(define-opcode irem 112 1 nil)
(define-opcode lrem 113 1 nil)
(define-opcode frem 114 1 nil)
(define-opcode drem 115 1 nil)
(define-opcode ineg 116 1 nil)
(define-opcode lneg 117 1 nil)
(define-opcode fneg 118 1 nil)
(define-opcode dneg 119 1 nil)
(define-opcode ishl 120 1 nil)
(define-opcode lshl 121 1 nil)
(define-opcode ishr 122 1 nil)
(define-opcode lshr 123 1 nil)
(define-opcode iushr 124 1 nil)
(define-opcode lushr 125 1 nil)
(define-opcode iand 126 1 nil)
(define-opcode land 127 1 nil)
(define-opcode ior 128 1 nil)
(define-opcode lor 129 1 nil)
(define-opcode ixor 130 1 nil)
(define-opcode lxor 131 1 nil)
(define-opcode iinc 132 3 nil)
(define-opcode i2l 133 1 nil)
(define-opcode i2f 134 1 nil)
(define-opcode i2d 135 1 nil)
(define-opcode l2i 136 1 nil)
(define-opcode l2f 137 1 nil)
(define-opcode l2d 138 1 nil)
(define-opcode f2i 139 1 nil)
(define-opcode f2l 140 1 nil)
(define-opcode f2d 141 1 nil)
(define-opcode d2i 142 1 nil)
(define-opcode d2l 143 1 nil)
(define-opcode d2f 144 1 nil)
(define-opcode i2b 145 1 nil)
(define-opcode i2c 146 1 nil)
(define-opcode i2s 147 1 nil)
(define-opcode lcmp 148 1 nil)
(define-opcode fcmpl 149 1 nil)
(define-opcode fcmpg 150 1 nil)
(define-opcode dcmpl 151 1 nil)
(define-opcode dcmpg 152 1 nil)
(define-opcode ifeq 153 3 -1)
(define-opcode ifne 154 3 -1)
(define-opcode iflt 155 3 nil)
(define-opcode ifge 156 3 nil)
(define-opcode ifgt 157 3 nil)
(define-opcode ifle 158 3 nil)
(define-opcode if_icmpeq 159 3 nil)
(define-opcode if_icmpne 160 3 nil)
(define-opcode if_icmplt 161 3 nil)
(define-opcode if_icmpge 162 3 nil)
(define-opcode if_icmpgt 163 3 nil)
(define-opcode if_icmple 164 3 nil)
(define-opcode if_acmpeq 165 3 -2)
(define-opcode if_acmpne 166 3 -2)
(define-opcode goto 167 3 0)
(define-opcode jsr 168 3 nil)
(define-opcode ret 169 2 nil)
(define-opcode tableswitch 170 0 nil)
(define-opcode lookupswitch 171 0 nil)
(define-opcode ireturn 172 1 nil)
(define-opcode lreturn 173 1 nil)
(define-opcode freturn 174 1 nil)
(define-opcode dreturn 175 1 nil)
(define-opcode areturn 176 1 -1)
(define-opcode return 177 1 0)
(define-opcode getstatic 178 3 1)
(define-opcode putstatic 179 3 -1)
(define-opcode getfield 180 3 0)
(define-opcode putfield 181 3 -2)
(define-opcode invokevirtual 182 3 nil)
(define-opcode invokespecial 183 3 nil)
(define-opcode invokestatic 184 3 nil)
(define-opcode invokeinterface 185 5 nil)
(define-opcode unused 186 0 nil)
(define-opcode new 187 3 1)
(define-opcode newarray 188 2 nil)
(define-opcode anewarray 189 3 0)
(define-opcode arraylength 190 1 nil)
(define-opcode athrow 191 1 0)
(define-opcode checkcast 192 3 0)
(define-opcode instanceof 193 3 0)
(define-opcode monitorenter 194 1 nil)
(define-opcode monitorexit 195 1 nil)
(define-opcode wide 196 0 nil)
(define-opcode multianewarray 197 4 nil)
(define-opcode ifnull 198 3 nil)
(define-opcode ifnonnull 199 3 nil)
(define-opcode goto_w 200 5 nil)
(define-opcode jsr_w 201 5 nil)
(define-opcode label 202 0 0)
(define-opcode push-value 203 nil 1)
(define-opcode store-value 204 nil -1)
(define-opcode clear-values 205 nil 0)
(define-opcode var-ref 206 nil nil)
(define-opcode var-set 207 nil nil)

(defparameter *last-opcode* 205)

(defun opcode-name (opcode-number)
  (let ((opcode (gethash opcode-number *opcodes*)))
    (and opcode (jvm-opcode-name opcode))))

(defun opcode-number (opcode-name)
  (let ((opcode (gethash (string opcode-name) *opcodes*)))
    (and opcode (jvm-opcode-number opcode))))

(defun opcode-size (opcode-name-or-number)
  (unless (numberp opcode-name-or-number)
    (setf opcode-name-or-number (string opcode-name-or-number)))
  (let ((opcode (gethash opcode-name-or-number *opcodes*)))
    (and opcode (jvm-opcode-size opcode))))

(defun opcode-stack-effect (opcode-name-or-number)
  (unless (numberp opcode-name-or-number)
    (setf opcode-name-or-number (string opcode-name-or-number)))
  (let ((opcode (gethash opcode-name-or-number *opcodes*)))
    (and opcode (jvm-opcode-stack-effect opcode))))

(provide '#:opcodes)
