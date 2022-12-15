package me.jingwang.test.log

import com.google.common.io.Resources
import me.jingwang.io.PathUtil
import me.jingwang.json.Coder
import me.jingwang.json.mask.MaskField
import org.junit.Test
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * Created by IntelliJ IDEA.
 * @Author : jingwang
 * @create 2022/9/21 4:20 PM
 *
 */
class LogTest {
    @Test
    fun testLog() {
        val logger = LoggerFactory.getLogger(javaClass)
        logger.info("hello world")
        logger.error("hello world")
    }

    @Test
    fun testMaskLog() {
        val logger = LoggerFactory.getLogger(javaClass)
        val a = Account("admin","password")
        val json = Coder.json.writeValueAsString(a)
        val prettyJson = Coder.json.writerWithDefaultPrettyPrinter().writeValueAsString(Account("admin","password"))
        logger.info(json)
        logger.info(prettyJson)
        val input = ActionInput(data = a)
        logger.info(Coder.json.writerWithDefaultPrettyPrinter().writeValueAsString(input))
        println(Coder.json.writerWithDefaultPrettyPrinter().writeValueAsString(input))
//        logger.info(test)
    }

    @Test
    fun testJsonMask() {
        val a = Account("admin","password")
        println(Coder.json.writeValueAsString(a))
    }

    @Test
    fun logMask() {
        val logger = LoggerFactory.getLogger(javaClass)
        logger.info(test)
    }

    @Test
    fun testNoMaskLog() {
        val noMaskLogger = LoggerFactory.getLogger("no.mask." + javaClass.name)
        noMaskLogger.info(test)
        val logger = LoggerFactory.getLogger(javaClass.name)
        logger.info(test)
    }

    @Test
    fun benchMarkLogCost() {
        val logger = LoggerFactory.getLogger(javaClass.name)
        val noMaskLogger = LoggerFactory.getLogger("no.mask." + javaClass.name)
        val logNums = 0
        val testLog = test
        val mask = measureTimeMillis {
            for (i in 0..logNums) {
                logger.info(testLog)
                logger.info("masking =========")
            }
        }

        val noMask = measureTimeMillis {
            for (i in 0..logNums) {
                noMaskLogger.info(testLog)
                logger.info("nomask =========")
            }
        }

        println("no mask time cost: $noMask ms")
        println("mask time cost: $mask ms")
        println("Mask $logNums log cost more times: ${mask - noMask}ms")
        println("second log length:" + testLog.length)
    }

    @Test
    fun testLogLengthLimit() {
        val testLog = "password: helloworld.  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa password: helloword"
        val logger = LoggerFactory.getLogger(javaClass.name)
        val noMaskLogger = LoggerFactory.getLogger("no.mask." + javaClass.name)
        logger.info(testLog)
        noMaskLogger.info(testLog)
    }

    @Test
    fun testS() {
        val regex = Regex("([0-9]{1,3}.){3}[0-9]{1,3}/[0-9]{1,2}")
        val s = regex.find("192.168.0.1/16")
        if (s != null) {
            println(s.value)
        }
    }
}

data class Account(
    val userName: String,
    @MaskField
    val password: String
)

class ActionInput(
    val data: Any
)

val test = """
 send notification: {
  "name" : "ProgressUpdate",
  "type" : "Event",
  "data" : {
    "actionUuid" : "ca0edc46530c47a2b0c3a4e590da392c",
    "actionName" : "login",
    "actionInput" : {
      "actionUuid" : "ca0edc46530c47a2b0c3a4e590da392c",
      "userName" : "admin",
      "password" : "4d7409ca38c29580225929f532ca67aa98d6d0fcc85595135f7bb03fe2480203324bf30edc5652d10a68615df22fc792a4c7e99fa745d74ad9734a64e5b16a29"
    },
    "actionOutput" : {
      "success" : false,
      "error" : {
        "code" : "OPERATION_FAILURE_ERROR",
        "description" : "Username or password maybe wrong",
        "details" : "Username or password maybe wrong",
        "opaque" : { }
      }
    },
    "resourceUuid" : "",
    "resourceName" : "",
    "state" : "Failed",
    "tasks" : [ ],
    "endTimeList" : [ ],
    "currentTask" : "",
    "percent" : -1,
    "createTime" : 1665561607885,
    "endTime" : 1665561607897,
    "success" : false,
    "error" : {
      "code" : "OPERATION_FAILURE_ERROR",
      "description" : "Username or password maybe wrong",
      "details" : "Username or password maybe wrong",
      "opaque" : { }
    }
  },
  "hasRead" : false,
  "uuid" : "944c0f38b0901f104c8a369efd0352c5",
  "createAt" : [ 2022, 10, 12, 16, 0, 7, 897029000 ],
  "occurTimes" : 1,
  "fullPrint" : true
}
""".trimIndent()

val testLog2 = """
    {api=5efe3a9fc62949bea0e4757fcc6a476e, job=5efe3a9fc62949bea0e4757fcc6a476e} - get net device info: {"id":"network:0","class":"network","claimed":true,"handle":"PCI:0000:19:00.0","description":"Ethernet interface","product":"82599ES 10-Gigabit SFI/SFP+ Network Connection","vendor":"Intel Corporation","physid":"0","businfo":"pci@0000:19:00.0","logicalname":"em1","version":"01","serial":"e4:43:4b:59:a8:b8","units":"bit/s","size":10000000000,"capacity":10000000000,"width":64,"clock":33000000,"configuration":{"autonegotiation":"on","broadcast":"yes","connector":"LC","driver":"ixgbe","driverversion":"4.18.0-348.23.1.1.ga8e8b87.el7.","duplex":"full","firmware":"0x8000095c, 18.8.9","latency":"0","link":"yes","maxlength":"300m","module":"FTLX8574D3BCV-IT","multicast":"yes","port":"fibre","slave":"yes","speed":"10Gbit/s","wavelength":"850nm"},"capabilities":{"pm":"Power Management","msi":"Message Signalled Interrupts","msix":"MSI-X","pciexpress":"PCI Express","vpd":"Vital Product Data","bus_master":"bus mastering","cap_list":"PCI capabilities listing","rom":"extension ROM","ethernet":true,"physical":"Physical interface","fibre":"optical fibre","1000bt-fd":"1Gbit/s (full duplex)","10000bt-fd":"10Gbit/s (full duplex)","autonegotiation":"Auto-negotiation"}}
""".trimIndent()

val testLog3 = """
    get net device info: br_bond0_30
""".trimIndent()

val testLog4 = """
    {api=69c76845becd4b9684a1e81ef1862c22} - Complete action: pkg-loader, output: {"success":true,"data":["check zstack license","check time source configuration","check monitoring data capacity utilization","check root capacity utilization","check databases remote backup","check zsha2 state","check disaster recovery","check host real cpu status","check host memory load","check host cpu load","check disk root capacity used","get running vm info","check host status","check password length","check swap status","check host zombie process","check vm not running number","check vm cpu average utilization","check vm disk root utilization","check blue screen crash","check long stop vm","check host net dev","check network interface status","check L2Network nic (bond)","check network reachable","check management network packet loss","check storage network packet loss","check host hdd","check host ssd","check host raid state","check primary storage status","check primary storage use physical capacity","check ceph primary storage mons status","check ceph primary storage status","check primary storage heart beat network","check vm volume snapshot number","check backup storage status","check backup storage use physical capacity","check vm ha strategy","check host reserve memory","check memory over provisioning","check primary storage over provisioning","check primary storage threshold","check primary storage reserve capacity","check backup storage reserve capacity","check management status","check db sync consistency","check ha network reachability","check vm ha closed","check cloud main license expired","check upgrade disk space enough","check env yum source unique"]}
""".trimIndent()

fun randomString(size: Long) : String {
    val s = StringBuilder("")
    val random = Random(size)
    for (i in 0..size) {
        val c = ('a'.code + (random.nextInt(26))).toChar()
        s.append(c)
    }
    return s.toString()
}

fun repeatString(c: String, size: Long) : String {
    val s = StringBuilder("")
    for (i in 0..size) {
        s.append(c)
    }
    return s.toString()
}

val facts2 = """"""

val facts = """
    {
      "192.168.195.132" : {
        "ip" : "192.168.195.132",
        "facts" : {
          "lshw" : {
            "id" : "ubuntu",
            "class" : "system",
            "claimed" : true,
            "handle" : "DMI:0001",
            "description" : "Computer",
            "product" : "VMware Virtual Platform",
            "vendor" : "VMware, Inc.",
            "version" : "None",
            "serial" : "VMware-56 4d 3a 56 c9 55 ba 70-99 72 f4 7a 75 e2 9c f9",
            "width" : 4294967295,
            "configuration" : {
              "administrator_password" : "enabled",
              "boot" : "normal",
              "frontpanel_password" : "unknown",
              "keyboard_password" : "unknown",
              "power-on_password" : "disabled",
              "uuid" : "564D3A56-C955-BA70-9972-F47A75E29CF9"
            },
            "capabilities" : {
              "smbios-2.7" : "SMBIOS version 2.7",
              "dmi-2.7" : "DMI version 2.7",
              "smp" : "Symmetric Multi-Processing",
              "vsyscall32" : "32-bit processes"
            },
            "children" : [ {
              "id" : "core",
              "class" : "bus",
              "claimed" : true,
              "handle" : "DMI:0002",
              "description" : "Motherboard",
              "product" : "440BX Desktop Reference Platform",
              "vendor" : "Intel Corporation",
              "physid" : "0",
              "version" : "None",
              "serial" : "None",
              "children" : [ {
                "id" : "firmware",
                "class" : "memory",
                "claimed" : true,
                "description" : "BIOS",
                "vendor" : "Phoenix Technologies LTD",
                "physid" : "0",
                "version" : "6.00",
                "date" : "07/29/2019",
                "units" : "bytes",
                "size" : 88960,
                "capabilities" : {
                  "isa" : "ISA bus",
                  "pci" : "PCI bus",
                  "pcmcia" : "PCMCIA/PCCard",
                  "pnp" : "Plug-and-Play",
                  "apm" : "Advanced Power Management",
                  "upgrade" : "BIOS EEPROM can be upgraded",
                  "shadowing" : "BIOS shadowing",
                  "escd" : "ESCD",
                  "cdboot" : "Booting from CD-ROM/DVD",
                  "bootselect" : "Selectable boot path",
                  "edd" : "Enhanced Disk Drive extensions",
                  "int5printscreen" : "Print Screen key",
                  "int9keyboard" : "i8042 keyboard controller",
                  "int14serial" : "INT14 serial line control",
                  "int17printer" : "INT17 printer control",
                  "int10video" : "INT10 CGA/Mono video",
                  "acpi" : "ACPI",
                  "smartbattery" : "Smart battery",
                  "biosbootspecification" : "BIOS boot specification",
                  "netboot" : "Function-key initiated network service boot"
                }
              }, {
                "id" : "cpu:0",
                "class" : "processor",
                "claimed" : true,
                "handle" : "DMI:0004",
                "description" : "CPU",
                "product" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "vendor" : "Intel Corp.",
                "physid" : "1",
                "businfo" : "cpu@0",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #000",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "width" : 64,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)",
                  "fpu" : "mathematical co-processor",
                  "fpu_exception" : "FPU exceptions reporting",
                  "wp" : true,
                  "vme" : "virtual mode extensions",
                  "de" : "debugging extensions",
                  "pse" : "page size extensions",
                  "tsc" : "time stamp counter",
                  "msr" : "model-specific registers",
                  "pae" : "4GB+ memory addressing (Physical Address Extension)",
                  "mce" : "machine check exceptions",
                  "cx8" : "compare and exchange 8-byte",
                  "apic" : "on-chip advanced programmable interrupt controller (APIC)",
                  "sep" : "fast system calls",
                  "mtrr" : "memory type range registers",
                  "pge" : "page global enable",
                  "mca" : "machine check architecture",
                  "cmov" : "conditional move instruction",
                  "pat" : "page attribute table",
                  "pse36" : "36-bit page size extensions",
                  "clflush" : true,
                  "mmx" : "multimedia extensions (MMX)",
                  "fxsr" : "fast floating point save/restore",
                  "sse" : "streaming SIMD extensions (SSE)",
                  "sse2" : "streaming SIMD extensions (SSE2)",
                  "ss" : "self-snoop",
                  "syscall" : "fast system calls",
                  "nx" : "no-execute bit (NX)",
                  "pdpe1gb" : true,
                  "rdtscp" : true,
                  "constant_tsc" : true,
                  "arch_perfmon" : true,
                  "nopl" : true,
                  "xtopology" : true,
                  "tsc_reliable" : true,
                  "nonstop_tsc" : true,
                  "cpuid" : true,
                  "pni" : true,
                  "pclmulqdq" : true,
                  "vmx" : true,
                  "ssse3" : true,
                  "fma" : true,
                  "cx16" : true,
                  "pcid" : true,
                  "sse4_1" : true,
                  "sse4_2" : true,
                  "x2apic" : true,
                  "movbe" : true,
                  "popcnt" : true,
                  "tsc_deadline_timer" : true,
                  "aes" : true,
                  "xsave" : true,
                  "avx" : true,
                  "f16c" : true,
                  "rdrand" : true,
                  "hypervisor" : true,
                  "lahf_lm" : true,
                  "abm" : true,
                  "3dnowprefetch" : true,
                  "cpuid_fault" : true,
                  "invpcid_single" : true,
                  "pti" : true,
                  "ssbd" : true,
                  "ibrs" : true,
                  "ibpb" : true,
                  "stibp" : true,
                  "tpr_shadow" : true,
                  "vnmi" : true,
                  "ept" : true,
                  "vpid" : true,
                  "ept_ad" : true,
                  "fsgsbase" : true,
                  "tsc_adjust" : true,
                  "bmi1" : true,
                  "avx2" : true,
                  "smep" : true,
                  "bmi2" : true,
                  "invpcid" : true,
                  "mpx" : true,
                  "rdseed" : true,
                  "adx" : true,
                  "smap" : true,
                  "clflushopt" : true,
                  "xsaveopt" : true,
                  "xsavec" : true,
                  "xsaves" : true,
                  "arat" : true,
                  "md_clear" : true,
                  "flush_l1d" : true,
                  "arch_capabilities" : true
                },
                "children" : [ {
                  "id" : "cache:0",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0094",
                  "description" : "L1 cache",
                  "physid" : "0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                }, {
                  "id" : "cache:1",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0094",
                  "description" : "L1 cache",
                  "physid" : "1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:1",
                "class" : "processor",
                "claimed" : true,
                "handle" : "DMI:0004",
                "description" : "CPU",
                "product" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "vendor" : "Intel Corp.",
                "physid" : "2",
                "businfo" : "cpu@1",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #001",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "width" : 64,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)",
                  "fpu" : "mathematical co-processor",
                  "fpu_exception" : "FPU exceptions reporting",
                  "wp" : true,
                  "vme" : "virtual mode extensions",
                  "de" : "debugging extensions",
                  "pse" : "page size extensions",
                  "tsc" : "time stamp counter",
                  "msr" : "model-specific registers",
                  "pae" : "4GB+ memory addressing (Physical Address Extension)",
                  "mce" : "machine check exceptions",
                  "cx8" : "compare and exchange 8-byte",
                  "apic" : "on-chip advanced programmable interrupt controller (APIC)",
                  "sep" : "fast system calls",
                  "mtrr" : "memory type range registers",
                  "pge" : "page global enable",
                  "mca" : "machine check architecture",
                  "cmov" : "conditional move instruction",
                  "pat" : "page attribute table",
                  "pse36" : "36-bit page size extensions",
                  "clflush" : true,
                  "mmx" : "multimedia extensions (MMX)",
                  "fxsr" : "fast floating point save/restore",
                  "sse" : "streaming SIMD extensions (SSE)",
                  "sse2" : "streaming SIMD extensions (SSE2)",
                  "ss" : "self-snoop",
                  "syscall" : "fast system calls",
                  "nx" : "no-execute bit (NX)",
                  "pdpe1gb" : true,
                  "rdtscp" : true,
                  "constant_tsc" : true,
                  "arch_perfmon" : true,
                  "nopl" : true,
                  "xtopology" : true,
                  "tsc_reliable" : true,
                  "nonstop_tsc" : true,
                  "cpuid" : true,
                  "pni" : true,
                  "pclmulqdq" : true,
                  "vmx" : true,
                  "ssse3" : true,
                  "fma" : true,
                  "cx16" : true,
                  "pcid" : true,
                  "sse4_1" : true,
                  "sse4_2" : true,
                  "x2apic" : true,
                  "movbe" : true,
                  "popcnt" : true,
                  "tsc_deadline_timer" : true,
                  "aes" : true,
                  "xsave" : true,
                  "avx" : true,
                  "f16c" : true,
                  "rdrand" : true,
                  "hypervisor" : true,
                  "lahf_lm" : true,
                  "abm" : true,
                  "3dnowprefetch" : true,
                  "cpuid_fault" : true,
                  "invpcid_single" : true,
                  "pti" : true,
                  "ssbd" : true,
                  "ibrs" : true,
                  "ibpb" : true,
                  "stibp" : true,
                  "tpr_shadow" : true,
                  "vnmi" : true,
                  "ept" : true,
                  "vpid" : true,
                  "ept_ad" : true,
                  "fsgsbase" : true,
                  "tsc_adjust" : true,
                  "bmi1" : true,
                  "avx2" : true,
                  "smep" : true,
                  "bmi2" : true,
                  "invpcid" : true,
                  "mpx" : true,
                  "rdseed" : true,
                  "adx" : true,
                  "smap" : true,
                  "clflushopt" : true,
                  "xsaveopt" : true,
                  "xsavec" : true,
                  "xsaves" : true,
                  "arat" : true,
                  "md_clear" : true,
                  "flush_l1d" : true,
                  "arch_capabilities" : true
                }
              }, {
                "id" : "cpu:2",
                "class" : "processor",
                "claimed" : true,
                "handle" : "DMI:0005",
                "description" : "CPU",
                "product" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "vendor" : "Intel Corp.",
                "physid" : "5",
                "businfo" : "cpu@2",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #002",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "width" : 64,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)",
                  "fpu" : "mathematical co-processor",
                  "fpu_exception" : "FPU exceptions reporting",
                  "wp" : true,
                  "vme" : "virtual mode extensions",
                  "de" : "debugging extensions",
                  "pse" : "page size extensions",
                  "tsc" : "time stamp counter",
                  "msr" : "model-specific registers",
                  "pae" : "4GB+ memory addressing (Physical Address Extension)",
                  "mce" : "machine check exceptions",
                  "cx8" : "compare and exchange 8-byte",
                  "apic" : "on-chip advanced programmable interrupt controller (APIC)",
                  "sep" : "fast system calls",
                  "mtrr" : "memory type range registers",
                  "pge" : "page global enable",
                  "mca" : "machine check architecture",
                  "cmov" : "conditional move instruction",
                  "pat" : "page attribute table",
                  "pse36" : "36-bit page size extensions",
                  "clflush" : true,
                  "mmx" : "multimedia extensions (MMX)",
                  "fxsr" : "fast floating point save/restore",
                  "sse" : "streaming SIMD extensions (SSE)",
                  "sse2" : "streaming SIMD extensions (SSE2)",
                  "ss" : "self-snoop",
                  "syscall" : "fast system calls",
                  "nx" : "no-execute bit (NX)",
                  "pdpe1gb" : true,
                  "rdtscp" : true,
                  "constant_tsc" : true,
                  "arch_perfmon" : true,
                  "nopl" : true,
                  "xtopology" : true,
                  "tsc_reliable" : true,
                  "nonstop_tsc" : true,
                  "cpuid" : true,
                  "pni" : true,
                  "pclmulqdq" : true,
                  "vmx" : true,
                  "ssse3" : true,
                  "fma" : true,
                  "cx16" : true,
                  "pcid" : true,
                  "sse4_1" : true,
                  "sse4_2" : true,
                  "x2apic" : true,
                  "movbe" : true,
                  "popcnt" : true,
                  "tsc_deadline_timer" : true,
                  "aes" : true,
                  "xsave" : true,
                  "avx" : true,
                  "f16c" : true,
                  "rdrand" : true,
                  "hypervisor" : true,
                  "lahf_lm" : true,
                  "abm" : true,
                  "3dnowprefetch" : true,
                  "cpuid_fault" : true,
                  "invpcid_single" : true,
                  "pti" : true,
                  "ssbd" : true,
                  "ibrs" : true,
                  "ibpb" : true,
                  "stibp" : true,
                  "tpr_shadow" : true,
                  "vnmi" : true,
                  "ept" : true,
                  "vpid" : true,
                  "ept_ad" : true,
                  "fsgsbase" : true,
                  "tsc_adjust" : true,
                  "bmi1" : true,
                  "avx2" : true,
                  "smep" : true,
                  "bmi2" : true,
                  "invpcid" : true,
                  "mpx" : true,
                  "rdseed" : true,
                  "adx" : true,
                  "smap" : true,
                  "clflushopt" : true,
                  "xsaveopt" : true,
                  "xsavec" : true,
                  "xsaves" : true,
                  "arat" : true,
                  "md_clear" : true,
                  "flush_l1d" : true,
                  "arch_capabilities" : true
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0095",
                  "description" : "L1 cache",
                  "physid" : "95",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:3",
                "class" : "processor",
                "claimed" : true,
                "handle" : "DMI:0006",
                "description" : "CPU",
                "product" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "vendor" : "Intel Corp.",
                "physid" : "6",
                "businfo" : "cpu@3",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #003",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "width" : 64,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)",
                  "fpu" : "mathematical co-processor",
                  "fpu_exception" : "FPU exceptions reporting",
                  "wp" : true,
                  "vme" : "virtual mode extensions",
                  "de" : "debugging extensions",
                  "pse" : "page size extensions",
                  "tsc" : "time stamp counter",
                  "msr" : "model-specific registers",
                  "pae" : "4GB+ memory addressing (Physical Address Extension)",
                  "mce" : "machine check exceptions",
                  "cx8" : "compare and exchange 8-byte",
                  "apic" : "on-chip advanced programmable interrupt controller (APIC)",
                  "sep" : "fast system calls",
                  "mtrr" : "memory type range registers",
                  "pge" : "page global enable",
                  "mca" : "machine check architecture",
                  "cmov" : "conditional move instruction",
                  "pat" : "page attribute table",
                  "pse36" : "36-bit page size extensions",
                  "clflush" : true,
                  "mmx" : "multimedia extensions (MMX)",
                  "fxsr" : "fast floating point save/restore",
                  "sse" : "streaming SIMD extensions (SSE)",
                  "sse2" : "streaming SIMD extensions (SSE2)",
                  "ss" : "self-snoop",
                  "syscall" : "fast system calls",
                  "nx" : "no-execute bit (NX)",
                  "pdpe1gb" : true,
                  "rdtscp" : true,
                  "constant_tsc" : true,
                  "arch_perfmon" : true,
                  "nopl" : true,
                  "xtopology" : true,
                  "tsc_reliable" : true,
                  "nonstop_tsc" : true,
                  "cpuid" : true,
                  "pni" : true,
                  "pclmulqdq" : true,
                  "vmx" : true,
                  "ssse3" : true,
                  "fma" : true,
                  "cx16" : true,
                  "pcid" : true,
                  "sse4_1" : true,
                  "sse4_2" : true,
                  "x2apic" : true,
                  "movbe" : true,
                  "popcnt" : true,
                  "tsc_deadline_timer" : true,
                  "aes" : true,
                  "xsave" : true,
                  "avx" : true,
                  "f16c" : true,
                  "rdrand" : true,
                  "hypervisor" : true,
                  "lahf_lm" : true,
                  "abm" : true,
                  "3dnowprefetch" : true,
                  "cpuid_fault" : true,
                  "invpcid_single" : true,
                  "pti" : true,
                  "ssbd" : true,
                  "ibrs" : true,
                  "ibpb" : true,
                  "stibp" : true,
                  "tpr_shadow" : true,
                  "vnmi" : true,
                  "ept" : true,
                  "vpid" : true,
                  "ept_ad" : true,
                  "fsgsbase" : true,
                  "tsc_adjust" : true,
                  "bmi1" : true,
                  "avx2" : true,
                  "smep" : true,
                  "bmi2" : true,
                  "invpcid" : true,
                  "mpx" : true,
                  "rdseed" : true,
                  "adx" : true,
                  "smap" : true,
                  "clflushopt" : true,
                  "xsaveopt" : true,
                  "xsavec" : true,
                  "xsaves" : true,
                  "arat" : true,
                  "md_clear" : true,
                  "flush_l1d" : true,
                  "arch_capabilities" : true
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0096",
                  "description" : "L1 cache",
                  "physid" : "96",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:4",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0007",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7",
                "businfo" : "cpu@4",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #004",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0097",
                  "description" : "L1 cache",
                  "physid" : "97",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:5",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0008",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "8",
                "businfo" : "cpu@5",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #005",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0098",
                  "description" : "L1 cache",
                  "physid" : "98",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:6",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0009",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "9",
                "businfo" : "cpu@6",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #006",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0099",
                  "description" : "L1 cache",
                  "physid" : "99",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:7",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:000A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "a",
                "businfo" : "cpu@7",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #007",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:009A",
                  "description" : "L1 cache",
                  "physid" : "9a",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:8",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:000B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "b",
                "businfo" : "cpu@8",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #008",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:009B",
                  "description" : "L1 cache",
                  "physid" : "9b",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:9",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:000C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "c",
                "businfo" : "cpu@9",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #009",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:009C",
                  "description" : "L1 cache",
                  "physid" : "9c",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:10",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:000D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "d",
                "businfo" : "cpu@10",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #010",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:009D",
                  "description" : "L1 cache",
                  "physid" : "9d",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:11",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:000E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "e",
                "businfo" : "cpu@11",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #011",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:009E",
                  "description" : "L1 cache",
                  "physid" : "9e",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:12",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:000F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "f",
                "businfo" : "cpu@12",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #012",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:009F",
                  "description" : "L1 cache",
                  "physid" : "9f",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:13",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0010",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "10",
                "businfo" : "cpu@13",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #013",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A0",
                  "description" : "L1 cache",
                  "physid" : "a0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:14",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0011",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "11",
                "businfo" : "cpu@14",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #014",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A1",
                  "description" : "L1 cache",
                  "physid" : "a1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:15",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0012",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "12",
                "businfo" : "cpu@15",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #015",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A2",
                  "description" : "L1 cache",
                  "physid" : "a2",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:16",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0013",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "13",
                "businfo" : "cpu@16",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #016",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A3",
                  "description" : "L1 cache",
                  "physid" : "a3",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:17",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0014",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "14",
                "businfo" : "cpu@17",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #017",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A4",
                  "description" : "L1 cache",
                  "physid" : "a4",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:18",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0015",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "15",
                "businfo" : "cpu@18",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #018",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A5",
                  "description" : "L1 cache",
                  "physid" : "a5",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:19",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0016",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "16",
                "businfo" : "cpu@19",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #019",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A6",
                  "description" : "L1 cache",
                  "physid" : "a6",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:20",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0017",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "17",
                "businfo" : "cpu@20",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #020",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A7",
                  "description" : "L1 cache",
                  "physid" : "a7",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:21",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0018",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "18",
                "businfo" : "cpu@21",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #021",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A8",
                  "description" : "L1 cache",
                  "physid" : "a8",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:22",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0019",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "19",
                "businfo" : "cpu@22",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #022",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00A9",
                  "description" : "L1 cache",
                  "physid" : "a9",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:23",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:001A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "1a",
                "businfo" : "cpu@23",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #023",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00AA",
                  "description" : "L1 cache",
                  "physid" : "aa",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:24",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:001B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "1b",
                "businfo" : "cpu@24",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #024",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00AB",
                  "description" : "L1 cache",
                  "physid" : "ab",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:25",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:001C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "1c",
                "businfo" : "cpu@25",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #025",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00AC",
                  "description" : "L1 cache",
                  "physid" : "ac",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:26",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:001D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "1d",
                "businfo" : "cpu@26",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #026",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00AD",
                  "description" : "L1 cache",
                  "physid" : "ad",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:27",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:001E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "1e",
                "businfo" : "cpu@27",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #027",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00AE",
                  "description" : "L1 cache",
                  "physid" : "ae",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:28",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:001F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "1f",
                "businfo" : "cpu@28",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #028",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00AF",
                  "description" : "L1 cache",
                  "physid" : "af",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:29",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0020",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "20",
                "businfo" : "cpu@29",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #029",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B0",
                  "description" : "L1 cache",
                  "physid" : "b0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:30",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0021",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "21",
                "businfo" : "cpu@30",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #030",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B1",
                  "description" : "L1 cache",
                  "physid" : "b1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:31",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0022",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "22",
                "businfo" : "cpu@31",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #031",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B2",
                  "description" : "L1 cache",
                  "physid" : "b2",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:32",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0023",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "23",
                "businfo" : "cpu@32",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #032",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B3",
                  "description" : "L1 cache",
                  "physid" : "b3",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:33",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0024",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "24",
                "businfo" : "cpu@33",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #033",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B4",
                  "description" : "L1 cache",
                  "physid" : "b4",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:34",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0025",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "25",
                "businfo" : "cpu@34",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #034",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B5",
                  "description" : "L1 cache",
                  "physid" : "b5",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                }, {
                  "id" : "bank:0",
                  "class" : "memory",
                  "handle" : "DMI:01E3",
                  "description" : "DIMM [empty]",
                  "physid" : "0",
                  "slot" : "NVD #0",
                  "width" : 32
                }, {
                  "id" : "bank:1",
                  "class" : "memory",
                  "handle" : "DMI:01E4",
                  "description" : "DIMM [empty]",
                  "physid" : "1",
                  "slot" : "NVD #1",
                  "width" : 32
                }, {
                  "id" : "bank:2",
                  "class" : "memory",
                  "handle" : "DMI:01E5",
                  "description" : "DIMM [empty]",
                  "physid" : "2",
                  "slot" : "NVD #2",
                  "width" : 32
                }, {
                  "id" : "bank:3",
                  "class" : "memory",
                  "handle" : "DMI:01E6",
                  "description" : "DIMM [empty]",
                  "physid" : "3",
                  "slot" : "NVD #3",
                  "width" : 32
                }, {
                  "id" : "bank:4",
                  "class" : "memory",
                  "handle" : "DMI:01E7",
                  "description" : "DIMM [empty]",
                  "physid" : "4",
                  "slot" : "NVD #4",
                  "width" : 32
                }, {
                  "id" : "bank:5",
                  "class" : "memory",
                  "handle" : "DMI:01E8",
                  "description" : "DIMM [empty]",
                  "physid" : "5",
                  "slot" : "NVD #5",
                  "width" : 32
                }, {
                  "id" : "bank:6",
                  "class" : "memory",
                  "handle" : "DMI:01E9",
                  "description" : "DIMM [empty]",
                  "physid" : "6",
                  "slot" : "NVD #6",
                  "width" : 32
                }, {
                  "id" : "bank:7",
                  "class" : "memory",
                  "handle" : "DMI:01EA",
                  "description" : "DIMM [empty]",
                  "physid" : "7",
                  "slot" : "NVD #7",
                  "width" : 32
                }, {
                  "id" : "bank:8",
                  "class" : "memory",
                  "handle" : "DMI:01EB",
                  "description" : "DIMM [empty]",
                  "physid" : "8",
                  "slot" : "NVD #8",
                  "width" : 32
                }, {
                  "id" : "bank:9",
                  "class" : "memory",
                  "handle" : "DMI:01EC",
                  "description" : "DIMM [empty]",
                  "physid" : "9",
                  "slot" : "NVD #9",
                  "width" : 32
                }, {
                  "id" : "bank:10",
                  "class" : "memory",
                  "handle" : "DMI:01ED",
                  "description" : "DIMM [empty]",
                  "physid" : "a",
                  "slot" : "NVD #10",
                  "width" : 32
                }, {
                  "id" : "bank:11",
                  "class" : "memory",
                  "handle" : "DMI:01EE",
                  "description" : "DIMM [empty]",
                  "physid" : "b",
                  "slot" : "NVD #11",
                  "width" : 32
                }, {
                  "id" : "bank:12",
                  "class" : "memory",
                  "handle" : "DMI:01EF",
                  "description" : "DIMM [empty]",
                  "physid" : "c",
                  "slot" : "NVD #12",
                  "width" : 32
                }, {
                  "id" : "bank:13",
                  "class" : "memory",
                  "handle" : "DMI:01F0",
                  "description" : "DIMM [empty]",
                  "physid" : "d",
                  "slot" : "NVD #13",
                  "width" : 32
                }, {
                  "id" : "bank:14",
                  "class" : "memory",
                  "handle" : "DMI:01F1",
                  "description" : "DIMM [empty]",
                  "physid" : "e",
                  "slot" : "NVD #14",
                  "width" : 32
                }, {
                  "id" : "bank:15",
                  "class" : "memory",
                  "handle" : "DMI:01F2",
                  "description" : "DIMM [empty]",
                  "physid" : "f",
                  "slot" : "NVD #15",
                  "width" : 32
                }, {
                  "id" : "bank:16",
                  "class" : "memory",
                  "handle" : "DMI:01F3",
                  "description" : "DIMM [empty]",
                  "physid" : "10",
                  "slot" : "NVD #16",
                  "width" : 32
                }, {
                  "id" : "bank:17",
                  "class" : "memory",
                  "handle" : "DMI:01F4",
                  "description" : "DIMM [empty]",
                  "physid" : "11",
                  "slot" : "NVD #17",
                  "width" : 32
                }, {
                  "id" : "bank:18",
                  "class" : "memory",
                  "handle" : "DMI:01F5",
                  "description" : "DIMM [empty]",
                  "physid" : "12",
                  "slot" : "NVD #18",
                  "width" : 32
                }, {
                  "id" : "bank:19",
                  "class" : "memory",
                  "handle" : "DMI:01F6",
                  "description" : "DIMM [empty]",
                  "physid" : "13",
                  "slot" : "NVD #19",
                  "width" : 32
                }, {
                  "id" : "bank:20",
                  "class" : "memory",
                  "handle" : "DMI:01F7",
                  "description" : "DIMM [empty]",
                  "physid" : "14",
                  "slot" : "NVD #20",
                  "width" : 32
                }, {
                  "id" : "bank:21",
                  "class" : "memory",
                  "handle" : "DMI:01F8",
                  "description" : "DIMM [empty]",
                  "physid" : "15",
                  "slot" : "NVD #21",
                  "width" : 32
                }, {
                  "id" : "bank:22",
                  "class" : "memory",
                  "handle" : "DMI:01F9",
                  "description" : "DIMM [empty]",
                  "physid" : "16",
                  "slot" : "NVD #22",
                  "width" : 32
                }, {
                  "id" : "bank:23",
                  "class" : "memory",
                  "handle" : "DMI:01FA",
                  "description" : "DIMM [empty]",
                  "physid" : "17",
                  "slot" : "NVD #23",
                  "width" : 32
                }, {
                  "id" : "bank:24",
                  "class" : "memory",
                  "handle" : "DMI:01FB",
                  "description" : "DIMM [empty]",
                  "physid" : "18",
                  "slot" : "NVD #24",
                  "width" : 32
                }, {
                  "id" : "bank:25",
                  "class" : "memory",
                  "handle" : "DMI:01FC",
                  "description" : "DIMM [empty]",
                  "physid" : "19",
                  "slot" : "NVD #25",
                  "width" : 32
                }, {
                  "id" : "bank:26",
                  "class" : "memory",
                  "handle" : "DMI:01FD",
                  "description" : "DIMM [empty]",
                  "physid" : "1a",
                  "slot" : "NVD #26",
                  "width" : 32
                }, {
                  "id" : "bank:27",
                  "class" : "memory",
                  "handle" : "DMI:01FE",
                  "description" : "DIMM [empty]",
                  "physid" : "1b",
                  "slot" : "NVD #27",
                  "width" : 32
                }, {
                  "id" : "bank:28",
                  "class" : "memory",
                  "handle" : "DMI:01FF",
                  "description" : "DIMM [empty]",
                  "physid" : "1c",
                  "slot" : "NVD #28",
                  "width" : 32
                }, {
                  "id" : "bank:29",
                  "class" : "memory",
                  "handle" : "DMI:0200",
                  "description" : "DIMM [empty]",
                  "physid" : "1d",
                  "slot" : "NVD #29",
                  "width" : 32
                }, {
                  "id" : "bank:30",
                  "class" : "memory",
                  "handle" : "DMI:0201",
                  "description" : "DIMM [empty]",
                  "physid" : "1e",
                  "slot" : "NVD #30",
                  "width" : 32
                }, {
                  "id" : "bank:31",
                  "class" : "memory",
                  "handle" : "DMI:0202",
                  "description" : "DIMM [empty]",
                  "physid" : "1f",
                  "slot" : "NVD #31",
                  "width" : 32
                }, {
                  "id" : "bank:32",
                  "class" : "memory",
                  "handle" : "DMI:0203",
                  "description" : "DIMM [empty]",
                  "physid" : "20",
                  "slot" : "NVD #32",
                  "width" : 32
                }, {
                  "id" : "bank:33",
                  "class" : "memory",
                  "handle" : "DMI:0204",
                  "description" : "DIMM [empty]",
                  "physid" : "21",
                  "slot" : "NVD #33",
                  "width" : 32
                }, {
                  "id" : "bank:34",
                  "class" : "memory",
                  "handle" : "DMI:0205",
                  "description" : "DIMM [empty]",
                  "physid" : "22",
                  "slot" : "NVD #34",
                  "width" : 32
                }, {
                  "id" : "bank:35",
                  "class" : "memory",
                  "handle" : "DMI:0206",
                  "description" : "DIMM [empty]",
                  "physid" : "23",
                  "slot" : "NVD #35",
                  "width" : 32
                }, {
                  "id" : "bank:36",
                  "class" : "memory",
                  "handle" : "DMI:0207",
                  "description" : "DIMM [empty]",
                  "physid" : "24",
                  "slot" : "NVD #36",
                  "width" : 32
                }, {
                  "id" : "bank:37",
                  "class" : "memory",
                  "handle" : "DMI:0208",
                  "description" : "DIMM [empty]",
                  "physid" : "25",
                  "slot" : "NVD #37",
                  "width" : 32
                }, {
                  "id" : "bank:38",
                  "class" : "memory",
                  "handle" : "DMI:0209",
                  "description" : "DIMM [empty]",
                  "physid" : "26",
                  "slot" : "NVD #38",
                  "width" : 32
                }, {
                  "id" : "bank:39",
                  "class" : "memory",
                  "handle" : "DMI:020A",
                  "description" : "DIMM [empty]",
                  "physid" : "27",
                  "slot" : "NVD #39",
                  "width" : 32
                }, {
                  "id" : "bank:40",
                  "class" : "memory",
                  "handle" : "DMI:020B",
                  "description" : "DIMM [empty]",
                  "physid" : "28",
                  "slot" : "NVD #40",
                  "width" : 32
                }, {
                  "id" : "bank:41",
                  "class" : "memory",
                  "handle" : "DMI:020C",
                  "description" : "DIMM [empty]",
                  "physid" : "29",
                  "slot" : "NVD #41",
                  "width" : 32
                }, {
                  "id" : "bank:42",
                  "class" : "memory",
                  "handle" : "DMI:020D",
                  "description" : "DIMM [empty]",
                  "physid" : "2a",
                  "slot" : "NVD #42",
                  "width" : 32
                }, {
                  "id" : "bank:43",
                  "class" : "memory",
                  "handle" : "DMI:020E",
                  "description" : "DIMM [empty]",
                  "physid" : "2b",
                  "slot" : "NVD #43",
                  "width" : 32
                }, {
                  "id" : "bank:44",
                  "class" : "memory",
                  "handle" : "DMI:020F",
                  "description" : "DIMM [empty]",
                  "physid" : "2c",
                  "slot" : "NVD #44",
                  "width" : 32
                }, {
                  "id" : "bank:45",
                  "class" : "memory",
                  "handle" : "DMI:0210",
                  "description" : "DIMM [empty]",
                  "physid" : "2d",
                  "slot" : "NVD #45",
                  "width" : 32
                }, {
                  "id" : "bank:46",
                  "class" : "memory",
                  "handle" : "DMI:0211",
                  "description" : "DIMM [empty]",
                  "physid" : "2e",
                  "slot" : "NVD #46",
                  "width" : 32
                }, {
                  "id" : "bank:47",
                  "class" : "memory",
                  "handle" : "DMI:0212",
                  "description" : "DIMM [empty]",
                  "physid" : "2f",
                  "slot" : "NVD #47",
                  "width" : 32
                }, {
                  "id" : "bank:48",
                  "class" : "memory",
                  "handle" : "DMI:0213",
                  "description" : "DIMM [empty]",
                  "physid" : "30",
                  "slot" : "NVD #48",
                  "width" : 32
                }, {
                  "id" : "bank:49",
                  "class" : "memory",
                  "handle" : "DMI:0214",
                  "description" : "DIMM [empty]",
                  "physid" : "31",
                  "slot" : "NVD #49",
                  "width" : 32
                }, {
                  "id" : "bank:50",
                  "class" : "memory",
                  "handle" : "DMI:0215",
                  "description" : "DIMM [empty]",
                  "physid" : "32",
                  "slot" : "NVD #50",
                  "width" : 32
                }, {
                  "id" : "bank:51",
                  "class" : "memory",
                  "handle" : "DMI:0216",
                  "description" : "DIMM [empty]",
                  "physid" : "33",
                  "slot" : "NVD #51",
                  "width" : 32
                }, {
                  "id" : "bank:52",
                  "class" : "memory",
                  "handle" : "DMI:0217",
                  "description" : "DIMM [empty]",
                  "physid" : "34",
                  "slot" : "NVD #52",
                  "width" : 32
                }, {
                  "id" : "bank:53",
                  "class" : "memory",
                  "handle" : "DMI:0218",
                  "description" : "DIMM [empty]",
                  "physid" : "35",
                  "slot" : "NVD #53",
                  "width" : 32
                }, {
                  "id" : "bank:54",
                  "class" : "memory",
                  "handle" : "DMI:0219",
                  "description" : "DIMM [empty]",
                  "physid" : "36",
                  "slot" : "NVD #54",
                  "width" : 32
                }, {
                  "id" : "bank:55",
                  "class" : "memory",
                  "handle" : "DMI:021A",
                  "description" : "DIMM [empty]",
                  "physid" : "37",
                  "slot" : "NVD #55",
                  "width" : 32
                }, {
                  "id" : "bank:56",
                  "class" : "memory",
                  "handle" : "DMI:021B",
                  "description" : "DIMM [empty]",
                  "physid" : "38",
                  "slot" : "NVD #56",
                  "width" : 32
                }, {
                  "id" : "bank:57",
                  "class" : "memory",
                  "handle" : "DMI:021C",
                  "description" : "DIMM [empty]",
                  "physid" : "39",
                  "slot" : "NVD #57",
                  "width" : 32
                }, {
                  "id" : "bank:58",
                  "class" : "memory",
                  "handle" : "DMI:021D",
                  "description" : "DIMM [empty]",
                  "physid" : "3a",
                  "slot" : "NVD #58",
                  "width" : 32
                }, {
                  "id" : "bank:59",
                  "class" : "memory",
                  "handle" : "DMI:021E",
                  "description" : "DIMM [empty]",
                  "physid" : "3b",
                  "slot" : "NVD #59",
                  "width" : 32
                }, {
                  "id" : "bank:60",
                  "class" : "memory",
                  "handle" : "DMI:021F",
                  "description" : "DIMM [empty]",
                  "physid" : "3c",
                  "slot" : "NVD #60",
                  "width" : 32
                }, {
                  "id" : "bank:61",
                  "class" : "memory",
                  "handle" : "DMI:0220",
                  "description" : "DIMM [empty]",
                  "physid" : "3d",
                  "slot" : "NVD #61",
                  "width" : 32
                }, {
                  "id" : "bank:62",
                  "class" : "memory",
                  "handle" : "DMI:0221",
                  "description" : "DIMM [empty]",
                  "physid" : "3e",
                  "slot" : "NVD #62",
                  "width" : 32
                }, {
                  "id" : "bank:63",
                  "class" : "memory",
                  "handle" : "DMI:0222",
                  "description" : "DIMM [empty]",
                  "physid" : "3f",
                  "slot" : "NVD #63",
                  "width" : 32
                } ]
              }, {
                "id" : "cpu:35",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0026",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "26",
                "businfo" : "cpu@35",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #035",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B6",
                  "description" : "L1 cache",
                  "physid" : "b6",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:36",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0027",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "27",
                "businfo" : "cpu@36",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #036",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B7",
                  "description" : "L1 cache",
                  "physid" : "b7",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:37",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0028",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "28",
                "businfo" : "cpu@37",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #037",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B8",
                  "description" : "L1 cache",
                  "physid" : "b8",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:38",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0029",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "29",
                "businfo" : "cpu@38",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #038",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00B9",
                  "description" : "L1 cache",
                  "physid" : "b9",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:39",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:002A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "2a",
                "businfo" : "cpu@39",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #039",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00BA",
                  "description" : "L1 cache",
                  "physid" : "ba",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:40",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:002B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "2b",
                "businfo" : "cpu@40",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #040",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00BB",
                  "description" : "L1 cache",
                  "physid" : "bb",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:41",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:002C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "2c",
                "businfo" : "cpu@41",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #041",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00BC",
                  "description" : "L1 cache",
                  "physid" : "bc",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:42",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:002D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "2d",
                "businfo" : "cpu@42",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #042",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00BD",
                  "description" : "L1 cache",
                  "physid" : "bd",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:43",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:002E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "2e",
                "businfo" : "cpu@43",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #043",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00BE",
                  "description" : "L1 cache",
                  "physid" : "be",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:44",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:002F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "2f",
                "businfo" : "cpu@44",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #044",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00BF",
                  "description" : "L1 cache",
                  "physid" : "bf",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:45",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0030",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "30",
                "businfo" : "cpu@45",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #045",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C0",
                  "description" : "L1 cache",
                  "physid" : "c0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:46",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0031",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "31",
                "businfo" : "cpu@46",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #046",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C1",
                  "description" : "L1 cache",
                  "physid" : "c1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:47",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0032",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "32",
                "businfo" : "cpu@47",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #047",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C2",
                  "description" : "L1 cache",
                  "physid" : "c2",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:48",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0033",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "33",
                "businfo" : "cpu@48",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #048",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C3",
                  "description" : "L1 cache",
                  "physid" : "c3",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:49",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0034",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "34",
                "businfo" : "cpu@49",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #049",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C4",
                  "description" : "L1 cache",
                  "physid" : "c4",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:50",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0035",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "35",
                "businfo" : "cpu@50",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #050",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C5",
                  "description" : "L1 cache",
                  "physid" : "c5",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:51",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0036",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "36",
                "businfo" : "cpu@51",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #051",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C6",
                  "description" : "L1 cache",
                  "physid" : "c6",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:52",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0037",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "37",
                "businfo" : "cpu@52",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #052",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C7",
                  "description" : "L1 cache",
                  "physid" : "c7",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:53",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0038",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "38",
                "businfo" : "cpu@53",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #053",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C8",
                  "description" : "L1 cache",
                  "physid" : "c8",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:54",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0039",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "39",
                "businfo" : "cpu@54",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #054",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00C9",
                  "description" : "L1 cache",
                  "physid" : "c9",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:55",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:003A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "3a",
                "businfo" : "cpu@55",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #055",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00CA",
                  "description" : "L1 cache",
                  "physid" : "ca",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:56",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:003B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "3b",
                "businfo" : "cpu@56",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #056",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00CB",
                  "description" : "L1 cache",
                  "physid" : "cb",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:57",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:003C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "3c",
                "businfo" : "cpu@57",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #057",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00CC",
                  "description" : "L1 cache",
                  "physid" : "cc",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:58",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:003D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "3d",
                "businfo" : "cpu@58",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #058",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00CD",
                  "description" : "L1 cache",
                  "physid" : "cd",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:59",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:003E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "3e",
                "businfo" : "cpu@59",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #059",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00CE",
                  "description" : "L1 cache",
                  "physid" : "ce",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:60",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:003F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "3f",
                "businfo" : "cpu@60",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #060",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00CF",
                  "description" : "L1 cache",
                  "physid" : "cf",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:61",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0040",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "40",
                "businfo" : "cpu@61",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #061",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D0",
                  "description" : "L1 cache",
                  "physid" : "d0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:62",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0041",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "41",
                "businfo" : "cpu@62",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #062",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D1",
                  "description" : "L1 cache",
                  "physid" : "d1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:63",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0042",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "42",
                "businfo" : "cpu@63",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #063",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D2",
                  "description" : "L1 cache",
                  "physid" : "d2",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:64",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0043",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "43",
                "businfo" : "cpu@64",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #064",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D3",
                  "description" : "L1 cache",
                  "physid" : "d3",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:65",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0044",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "44",
                "businfo" : "cpu@65",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #065",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D4",
                  "description" : "L1 cache",
                  "physid" : "d4",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:66",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0045",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "45",
                "businfo" : "cpu@66",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #066",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D5",
                  "description" : "L1 cache",
                  "physid" : "d5",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:67",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0046",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "46",
                "businfo" : "cpu@67",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #067",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D6",
                  "description" : "L1 cache",
                  "physid" : "d6",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:68",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0047",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "47",
                "businfo" : "cpu@68",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #068",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D7",
                  "description" : "L1 cache",
                  "physid" : "d7",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:69",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0048",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "48",
                "businfo" : "cpu@69",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #069",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D8",
                  "description" : "L1 cache",
                  "physid" : "d8",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:70",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0049",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "49",
                "businfo" : "cpu@70",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #070",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00D9",
                  "description" : "L1 cache",
                  "physid" : "d9",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:71",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:004A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "4a",
                "businfo" : "cpu@71",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #071",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00DA",
                  "description" : "L1 cache",
                  "physid" : "da",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:72",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:004B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "4b",
                "businfo" : "cpu@72",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #072",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00DB",
                  "description" : "L1 cache",
                  "physid" : "db",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:73",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:004C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "4c",
                "businfo" : "cpu@73",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #073",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00DC",
                  "description" : "L1 cache",
                  "physid" : "dc",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:74",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:004D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "4d",
                "businfo" : "cpu@74",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #074",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00DD",
                  "description" : "L1 cache",
                  "physid" : "dd",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:75",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:004E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "4e",
                "businfo" : "cpu@75",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #075",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00DE",
                  "description" : "L1 cache",
                  "physid" : "de",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:76",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:004F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "4f",
                "businfo" : "cpu@76",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #076",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00DF",
                  "description" : "L1 cache",
                  "physid" : "df",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:77",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0050",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "50",
                "businfo" : "cpu@77",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #077",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E0",
                  "description" : "L1 cache",
                  "physid" : "e0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:78",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0051",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "51",
                "businfo" : "cpu@78",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #078",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E1",
                  "description" : "L1 cache",
                  "physid" : "e1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:79",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0052",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "52",
                "businfo" : "cpu@79",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #079",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E2",
                  "description" : "L1 cache",
                  "physid" : "e2",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:80",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0053",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "53",
                "businfo" : "cpu@80",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #080",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E3",
                  "description" : "L1 cache",
                  "physid" : "e3",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:81",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0054",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "54",
                "businfo" : "cpu@81",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #081",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E4",
                  "description" : "L1 cache",
                  "physid" : "e4",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:82",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0055",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "55",
                "businfo" : "cpu@82",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #082",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E5",
                  "description" : "L1 cache",
                  "physid" : "e5",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:83",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0056",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "56",
                "businfo" : "cpu@83",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #083",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E6",
                  "description" : "L1 cache",
                  "physid" : "e6",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:84",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0057",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "57",
                "businfo" : "cpu@84",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #084",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E7",
                  "description" : "L1 cache",
                  "physid" : "e7",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:85",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0058",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "58",
                "businfo" : "cpu@85",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #085",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E8",
                  "description" : "L1 cache",
                  "physid" : "e8",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:86",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0059",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "59",
                "businfo" : "cpu@86",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #086",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00E9",
                  "description" : "L1 cache",
                  "physid" : "e9",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:87",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:005A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "5a",
                "businfo" : "cpu@87",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #087",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00EA",
                  "description" : "L1 cache",
                  "physid" : "ea",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:88",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:005B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "5b",
                "businfo" : "cpu@88",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #088",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00EB",
                  "description" : "L1 cache",
                  "physid" : "eb",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:89",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:005C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "5c",
                "businfo" : "cpu@89",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #089",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00EC",
                  "description" : "L1 cache",
                  "physid" : "ec",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:90",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:005D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "5d",
                "businfo" : "cpu@90",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #090",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00ED",
                  "description" : "L1 cache",
                  "physid" : "ed",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:91",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:005E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "5e",
                "businfo" : "cpu@91",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #091",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00EE",
                  "description" : "L1 cache",
                  "physid" : "ee",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:92",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:005F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "5f",
                "businfo" : "cpu@92",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #092",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00EF",
                  "description" : "L1 cache",
                  "physid" : "ef",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:93",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0060",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "60",
                "businfo" : "cpu@93",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #093",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F0",
                  "description" : "L1 cache",
                  "physid" : "f0",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:94",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0061",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "61",
                "businfo" : "cpu@94",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #094",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F1",
                  "description" : "L1 cache",
                  "physid" : "f1",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:95",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0062",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "62",
                "businfo" : "cpu@95",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #095",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F2",
                  "description" : "L1 cache",
                  "physid" : "f2",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:96",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0063",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "63",
                "businfo" : "cpu@96",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #096",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F3",
                  "description" : "L1 cache",
                  "physid" : "f3",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:97",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0064",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "64",
                "businfo" : "cpu@97",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #097",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F4",
                  "description" : "L1 cache",
                  "physid" : "f4",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:98",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0065",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "65",
                "businfo" : "cpu@98",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #098",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F5",
                  "description" : "L1 cache",
                  "physid" : "f5",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:99",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0066",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "66",
                "businfo" : "cpu@99",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #099",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F6",
                  "description" : "L1 cache",
                  "physid" : "f6",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:100",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0067",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "67",
                "businfo" : "cpu@100",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #100",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F7",
                  "description" : "L1 cache",
                  "physid" : "f7",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:101",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0068",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "68",
                "businfo" : "cpu@101",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #101",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F8",
                  "description" : "L1 cache",
                  "physid" : "f8",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:102",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0069",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "69",
                "businfo" : "cpu@102",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #102",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00F9",
                  "description" : "L1 cache",
                  "physid" : "f9",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:103",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:006A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "6a",
                "businfo" : "cpu@103",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #103",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00FA",
                  "description" : "L1 cache",
                  "physid" : "fa",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:104",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:006B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "6b",
                "businfo" : "cpu@104",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #104",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00FB",
                  "description" : "L1 cache",
                  "physid" : "fb",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:105",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:006C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "6c",
                "businfo" : "cpu@105",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #105",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00FC",
                  "description" : "L1 cache",
                  "physid" : "fc",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:106",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:006D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "6d",
                "businfo" : "cpu@106",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #106",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00FD",
                  "description" : "L1 cache",
                  "physid" : "fd",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:107",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:006E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "6e",
                "businfo" : "cpu@107",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #107",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00FE",
                  "description" : "L1 cache",
                  "physid" : "fe",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:108",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:006F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "6f",
                "businfo" : "cpu@108",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #108",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:00FF",
                  "description" : "L1 cache",
                  "physid" : "ff",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:109",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0070",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "70",
                "businfo" : "cpu@109",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #109",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0100",
                  "description" : "L1 cache",
                  "physid" : "100",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:110",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0071",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "71",
                "businfo" : "cpu@110",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #110",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0101",
                  "description" : "L1 cache",
                  "physid" : "101",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:111",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0072",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "72",
                "businfo" : "cpu@111",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #111",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0102",
                  "description" : "L1 cache",
                  "physid" : "102",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:112",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0073",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "73",
                "businfo" : "cpu@112",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #112",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0103",
                  "description" : "L1 cache",
                  "physid" : "103",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:113",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0074",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "74",
                "businfo" : "cpu@113",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #113",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0104",
                  "description" : "L1 cache",
                  "physid" : "104",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:114",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0075",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "75",
                "businfo" : "cpu@114",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #114",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0105",
                  "description" : "L1 cache",
                  "physid" : "105",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:115",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0076",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "76",
                "businfo" : "cpu@115",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #115",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0106",
                  "description" : "L1 cache",
                  "physid" : "106",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:116",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0077",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "77",
                "businfo" : "cpu@116",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #116",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0107",
                  "description" : "L1 cache",
                  "physid" : "107",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:117",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0078",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "78",
                "businfo" : "cpu@117",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #117",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0108",
                  "description" : "L1 cache",
                  "physid" : "108",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:118",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0079",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "79",
                "businfo" : "cpu@118",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #118",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0109",
                  "description" : "L1 cache",
                  "physid" : "109",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:119",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:007A",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7a",
                "businfo" : "cpu@119",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #119",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:010A",
                  "description" : "L1 cache",
                  "physid" : "10a",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:120",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:007B",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7b",
                "businfo" : "cpu@120",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #120",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:010B",
                  "description" : "L1 cache",
                  "physid" : "10b",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:121",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:007C",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7c",
                "businfo" : "cpu@121",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #121",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:010C",
                  "description" : "L1 cache",
                  "physid" : "10c",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:122",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:007D",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7d",
                "businfo" : "cpu@122",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #122",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:010D",
                  "description" : "L1 cache",
                  "physid" : "10d",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:123",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:007E",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7e",
                "businfo" : "cpu@123",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #123",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:010E",
                  "description" : "L1 cache",
                  "physid" : "10e",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:124",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:007F",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "7f",
                "businfo" : "cpu@124",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #124",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:010F",
                  "description" : "L1 cache",
                  "physid" : "10f",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:125",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0080",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "80",
                "businfo" : "cpu@125",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #125",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0110",
                  "description" : "L1 cache",
                  "physid" : "110",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:126",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0081",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "81",
                "businfo" : "cpu@126",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #126",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0111",
                  "description" : "L1 cache",
                  "physid" : "111",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "cpu:127",
                "class" : "processor",
                "disabled" : true,
                "claimed" : true,
                "handle" : "DMI:0082",
                "description" : "CPU",
                "vendor" : "GenuineIntel",
                "physid" : "82",
                "businfo" : "cpu@127",
                "version" : "Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz",
                "slot" : "CPU #127",
                "units" : "Hz",
                "size" : 2200000000,
                "capacity" : 4230196224,
                "configuration" : {
                  "cores" : "1",
                  "enabledcores" : "1"
                },
                "capabilities" : {
                  "x86-64" : "64bits extensions (x86-64)"
                },
                "children" : [ {
                  "id" : "cache",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:0112",
                  "description" : "L1 cache",
                  "physid" : "112",
                  "slot" : "L1",
                  "units" : "bytes",
                  "size" : 16384,
                  "capacity" : 16384,
                  "configuration" : {
                    "level" : "1"
                  },
                  "capabilities" : {
                    "asynchronous" : "Asynchronous",
                    "internal" : "Internal",
                    "write-back" : "Write-back"
                  }
                } ]
              }, {
                "id" : "memory",
                "class" : "memory",
                "claimed" : true,
                "handle" : "DMI:01A2",
                "description" : "System Memory",
                "physid" : "1a2",
                "slot" : "System board or motherboard",
                "units" : "bytes",
                "size" : 8589934592,
                "children" : [ {
                  "id" : "bank:0",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A3",
                  "description" : "DIMM DRAM EDO",
                  "physid" : "0",
                  "slot" : "RAM slot #0",
                  "units" : "bytes",
                  "size" : 8589934592,
                  "width" : 32
                }, {
                  "id" : "bank:1",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A4",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1",
                  "slot" : "RAM slot #1"
                }, {
                  "id" : "bank:2",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A5",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2",
                  "slot" : "RAM slot #2"
                }, {
                  "id" : "bank:3",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A6",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3",
                  "slot" : "RAM slot #3"
                }, {
                  "id" : "bank:4",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A7",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "4",
                  "slot" : "RAM slot #4"
                }, {
                  "id" : "bank:5",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A8",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "5",
                  "slot" : "RAM slot #5"
                }, {
                  "id" : "bank:6",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01A9",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "6",
                  "slot" : "RAM slot #6"
                }, {
                  "id" : "bank:7",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01AA",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "7",
                  "slot" : "RAM slot #7"
                }, {
                  "id" : "bank:8",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01AB",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "8",
                  "slot" : "RAM slot #8"
                }, {
                  "id" : "bank:9",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01AC",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "9",
                  "slot" : "RAM slot #9"
                }, {
                  "id" : "bank:10",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01AD",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "a",
                  "slot" : "RAM slot #10"
                }, {
                  "id" : "bank:11",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01AE",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "b",
                  "slot" : "RAM slot #11"
                }, {
                  "id" : "bank:12",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01AF",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "c",
                  "slot" : "RAM slot #12"
                }, {
                  "id" : "bank:13",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B0",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "d",
                  "slot" : "RAM slot #13"
                }, {
                  "id" : "bank:14",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B1",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "e",
                  "slot" : "RAM slot #14"
                }, {
                  "id" : "bank:15",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B2",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "f",
                  "slot" : "RAM slot #15"
                }, {
                  "id" : "bank:16",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B3",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "10",
                  "slot" : "RAM slot #16"
                }, {
                  "id" : "bank:17",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B4",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "11",
                  "slot" : "RAM slot #17"
                }, {
                  "id" : "bank:18",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B5",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "12",
                  "slot" : "RAM slot #18"
                }, {
                  "id" : "bank:19",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B6",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "13",
                  "slot" : "RAM slot #19"
                }, {
                  "id" : "bank:20",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B7",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "14",
                  "slot" : "RAM slot #20"
                }, {
                  "id" : "bank:21",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B8",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "15",
                  "slot" : "RAM slot #21"
                }, {
                  "id" : "bank:22",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01B9",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "16",
                  "slot" : "RAM slot #22"
                }, {
                  "id" : "bank:23",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01BA",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "17",
                  "slot" : "RAM slot #23"
                }, {
                  "id" : "bank:24",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01BB",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "18",
                  "slot" : "RAM slot #24"
                }, {
                  "id" : "bank:25",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01BC",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "19",
                  "slot" : "RAM slot #25"
                }, {
                  "id" : "bank:26",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01BD",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1a",
                  "slot" : "RAM slot #26"
                }, {
                  "id" : "bank:27",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01BE",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1b",
                  "slot" : "RAM slot #27"
                }, {
                  "id" : "bank:28",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01BF",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1c",
                  "slot" : "RAM slot #28"
                }, {
                  "id" : "bank:29",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C0",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1d",
                  "slot" : "RAM slot #29"
                }, {
                  "id" : "bank:30",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C1",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1e",
                  "slot" : "RAM slot #30"
                }, {
                  "id" : "bank:31",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C2",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "1f",
                  "slot" : "RAM slot #31"
                }, {
                  "id" : "bank:32",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C3",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "20",
                  "slot" : "RAM slot #32"
                }, {
                  "id" : "bank:33",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C4",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "21",
                  "slot" : "RAM slot #33"
                }, {
                  "id" : "bank:34",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C5",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "22",
                  "slot" : "RAM slot #34"
                }, {
                  "id" : "bank:35",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C6",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "23",
                  "slot" : "RAM slot #35"
                }, {
                  "id" : "bank:36",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C7",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "24",
                  "slot" : "RAM slot #36"
                }, {
                  "id" : "bank:37",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C8",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "25",
                  "slot" : "RAM slot #37"
                }, {
                  "id" : "bank:38",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01C9",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "26",
                  "slot" : "RAM slot #38"
                }, {
                  "id" : "bank:39",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01CA",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "27",
                  "slot" : "RAM slot #39"
                }, {
                  "id" : "bank:40",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01CB",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "28",
                  "slot" : "RAM slot #40"
                }, {
                  "id" : "bank:41",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01CC",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "29",
                  "slot" : "RAM slot #41"
                }, {
                  "id" : "bank:42",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01CD",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2a",
                  "slot" : "RAM slot #42"
                }, {
                  "id" : "bank:43",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01CE",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2b",
                  "slot" : "RAM slot #43"
                }, {
                  "id" : "bank:44",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01CF",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2c",
                  "slot" : "RAM slot #44"
                }, {
                  "id" : "bank:45",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D0",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2d",
                  "slot" : "RAM slot #45"
                }, {
                  "id" : "bank:46",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D1",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2e",
                  "slot" : "RAM slot #46"
                }, {
                  "id" : "bank:47",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D2",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "2f",
                  "slot" : "RAM slot #47"
                }, {
                  "id" : "bank:48",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D3",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "30",
                  "slot" : "RAM slot #48"
                }, {
                  "id" : "bank:49",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D4",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "31",
                  "slot" : "RAM slot #49"
                }, {
                  "id" : "bank:50",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D5",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "32",
                  "slot" : "RAM slot #50"
                }, {
                  "id" : "bank:51",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D6",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "33",
                  "slot" : "RAM slot #51"
                }, {
                  "id" : "bank:52",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D7",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "34",
                  "slot" : "RAM slot #52"
                }, {
                  "id" : "bank:53",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D8",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "35",
                  "slot" : "RAM slot #53"
                }, {
                  "id" : "bank:54",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01D9",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "36",
                  "slot" : "RAM slot #54"
                }, {
                  "id" : "bank:55",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01DA",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "37",
                  "slot" : "RAM slot #55"
                }, {
                  "id" : "bank:56",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01DB",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "38",
                  "slot" : "RAM slot #56"
                }, {
                  "id" : "bank:57",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01DC",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "39",
                  "slot" : "RAM slot #57"
                }, {
                  "id" : "bank:58",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01DD",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3a",
                  "slot" : "RAM slot #58"
                }, {
                  "id" : "bank:59",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01DE",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3b",
                  "slot" : "RAM slot #59"
                }, {
                  "id" : "bank:60",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01DF",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3c",
                  "slot" : "RAM slot #60"
                }, {
                  "id" : "bank:61",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01E0",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3d",
                  "slot" : "RAM slot #61"
                }, {
                  "id" : "bank:62",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01E1",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3e",
                  "slot" : "RAM slot #62"
                }, {
                  "id" : "bank:63",
                  "class" : "memory",
                  "claimed" : true,
                  "handle" : "DMI:01E2",
                  "description" : "DIMM DRAM [empty]",
                  "physid" : "3f",
                  "slot" : "RAM slot #63"
                } ]
              }, {
                "id" : "pci",
                "class" : "bridge",
                "claimed" : true,
                "handle" : "PCIBUS:0000:00",
                "description" : "Host bridge",
                "product" : "440BX/ZX/DX - 82443BX/ZX/DX Host bridge",
                "vendor" : "Intel Corporation",
                "physid" : "100",
                "businfo" : "pci@0000:00:00.0",
                "version" : "01",
                "width" : 32,
                "clock" : 33000000,
                "configuration" : {
                  "driver" : "agpgart-intel"
                },
                "children" : [ {
                  "id" : "pci:0",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:01",
                  "description" : "PCI bridge",
                  "product" : "440BX/ZX/DX - 82443BX/ZX/DX AGP bridge",
                  "vendor" : "Intel Corporation",
                  "physid" : "1",
                  "businfo" : "pci@0000:00:01.0",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 66000000,
                  "capabilities" : {
                    "pci" : true,
                    "normal_decode" : true,
                    "bus_master" : "bus mastering"
                  }
                }, {
                  "id" : "isa",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCI:0000:00:07.0",
                  "description" : "ISA bridge",
                  "product" : "82371AB/EB/MB PIIX4 ISA",
                  "vendor" : "Intel Corporation",
                  "physid" : "7",
                  "businfo" : "pci@0000:00:07.0",
                  "version" : "08",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "latency" : "0"
                  },
                  "capabilities" : {
                    "isa" : true,
                    "bus_master" : "bus mastering"
                  }
                }, {
                  "id" : "ide",
                  "class" : "storage",
                  "claimed" : true,
                  "handle" : "PCI:0000:00:07.1",
                  "description" : "IDE interface",
                  "product" : "82371AB/EB/MB PIIX4 IDE",
                  "vendor" : "Intel Corporation",
                  "physid" : "7.1",
                  "businfo" : "pci@0000:00:07.1",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "ata_piix",
                    "latency" : "64"
                  },
                  "capabilities" : {
                    "ide" : true,
                    "isa_compatibility_mode_controller__supports_both_channels_switched_to_pci_native_mode__supports_bus_mastering" : true,
                    "bus_master" : "bus mastering"
                  }
                }, {
                  "id" : "bridge",
                  "class" : "bridge",
                  "handle" : "PCI:0000:00:07.3",
                  "description" : "Bridge",
                  "product" : "82371AB/EB/MB PIIX4 ACPI",
                  "vendor" : "Intel Corporation",
                  "physid" : "7.3",
                  "businfo" : "pci@0000:00:07.3",
                  "version" : "08",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "latency" : "0"
                  },
                  "capabilities" : {
                    "bridge" : true
                  }
                }, {
                  "id" : "generic",
                  "class" : "generic",
                  "claimed" : true,
                  "handle" : "PCI:0000:00:07.7",
                  "description" : "System peripheral",
                  "product" : "Virtual Machine Communication Interface",
                  "vendor" : "VMware",
                  "physid" : "7.7",
                  "businfo" : "pci@0000:00:07.7",
                  "version" : "10",
                  "width" : 64,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "vmw_vmci",
                    "latency" : "64",
                    "maxlatency" : "255",
                    "mingnt" : "6"
                  },
                  "capabilities" : {
                    "msi" : "Message Signalled Interrupts",
                    "msix" : "MSI-X",
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "display",
                  "class" : "display",
                  "claimed" : true,
                  "handle" : "PCI:0000:00:0f.0",
                  "description" : "VGA compatible controller",
                  "product" : "SVGA II Adapter",
                  "vendor" : "VMware",
                  "physid" : "f",
                  "businfo" : "pci@0000:00:0f.0",
                  "version" : "00",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "vmwgfx",
                    "latency" : "64"
                  },
                  "capabilities" : {
                    "vga_controller" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing",
                    "rom" : "extension ROM"
                  }
                }, {
                  "id" : "scsi",
                  "class" : "storage",
                  "claimed" : true,
                  "handle" : "SCSI:32",
                  "description" : "SCSI storage controller",
                  "product" : "53c1030 PCI-X Fusion-MPT Dual Ultra320 SCSI",
                  "vendor" : "LSI Logic / Symbios Logic",
                  "physid" : "10",
                  "businfo" : "pci@0000:00:10.0",
                  "logicalname" : "scsi32",
                  "version" : "01",
                  "width" : 64,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "mptspi",
                    "latency" : "64",
                    "maxlatency" : "255",
                    "mingnt" : "6"
                  },
                  "capabilities" : {
                    "scsi" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing",
                    "rom" : "extension ROM",
                    "scsi-host" : "SCSI host adapter"
                  },
                  "children" : [ {
                    "id" : "disk",
                    "class" : "disk",
                    "claimed" : true,
                    "handle" : "SCSI:32:00:00:00",
                    "description" : "SCSI Disk",
                    "product" : "VMware Virtual S",
                    "vendor" : "VMware,",
                    "physid" : "0.0.0",
                    "businfo" : "scsi@32:0.0.0",
                    "logicalname" : "/dev/sda",
                    "dev" : "8:0",
                    "version" : "1.0",
                    "units" : "bytes",
                    "size" : 214748364800,
                    "configuration" : {
                      "ansiversion" : "2",
                      "logicalsectorsize" : "512",
                      "sectorsize" : "512",
                      "signature" : "ea211969"
                    },
                    "capabilities" : {
                      "7200rpm" : "7200 rotations per minute",
                      "partitioned" : "Partitioned disk",
                      "partitioned:dos" : "MS-DOS partition table"
                    },
                    "children" : [ {
                      "id" : "volume",
                      "class" : "volume",
                      "claimed" : true,
                      "description" : "EXT4 volume",
                      "vendor" : "Linux",
                      "physid" : "1",
                      "businfo" : "scsi@32:0.0.0,1",
                      "logicalname" : [ "/dev/sda1", "/" ],
                      "dev" : "8:1",
                      "version" : "1.0",
                      "serial" : "91aa8583-f97d-412b-9ec3-8b61b6ecbc96",
                      "size" : 214746267648,
                      "capacity" : 214746267648,
                      "configuration" : {
                        "created" : "2020-02-18 08:18:28",
                        "filesystem" : "ext4",
                        "lastmountpoint" : "/",
                        "modified" : "2020-03-19 21:58:17",
                        "mount.fstype" : "ext4",
                        "mount.options" : "rw,relatime,errors=remount-ro",
                        "mounted" : "2020-03-18 08:10:18",
                        "state" : "mounted"
                      },
                      "capabilities" : {
                        "primary" : "Primary partition",
                        "bootable" : "Bootable partition (active)",
                        "journaled" : true,
                        "extended_attributes" : "Extended Attributes",
                        "large_files" : "4GB+ files",
                        "huge_files" : "16TB+ files",
                        "dir_nlink" : "directories with 65000+ subdirs",
                        "64bit" : "64bit filesystem",
                        "extents" : "extent-based allocation",
                        "ext4" : true,
                        "ext2" : "EXT2/EXT3",
                        "initialized" : "initialized volume"
                      }
                    } ]
                  } ]
                }, {
                  "id" : "pci:1",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:02",
                  "description" : "PCI bridge",
                  "product" : "PCI bridge",
                  "vendor" : "VMware",
                  "physid" : "11",
                  "businfo" : "pci@0000:00:11.0",
                  "version" : "02",
                  "width" : 32,
                  "clock" : 33000000,
                  "capabilities" : {
                    "pci" : true,
                    "subtractive_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  },
                  "children" : [ {
                    "id" : "usb:0",
                    "class" : "bus",
                    "claimed" : true,
                    "handle" : "PCI:0000:02:00.0",
                    "description" : "USB controller",
                    "product" : "USB1.1 UHCI Controller",
                    "vendor" : "VMware",
                    "physid" : "0",
                    "businfo" : "pci@0000:02:00.0",
                    "version" : "00",
                    "width" : 32,
                    "clock" : 33000000,
                    "configuration" : {
                      "driver" : "uhci_hcd",
                      "latency" : "64"
                    },
                    "capabilities" : {
                      "uhci" : "Universal Host Controller Interface (USB1)",
                      "bus_master" : "bus mastering",
                      "cap_list" : "PCI capabilities listing"
                    },
                    "children" : [ {
                      "id" : "usbhost",
                      "class" : "bus",
                      "claimed" : true,
                      "handle" : "USB:2:1",
                      "product" : "UHCI Host Controller",
                      "vendor" : "Linux 5.3.0-40-generic uhci_hcd",
                      "physid" : "1",
                      "businfo" : "usb@2",
                      "logicalname" : "usb2",
                      "version" : "5.03",
                      "configuration" : {
                        "driver" : "hub",
                        "slots" : "2",
                        "speed" : "12Mbit/s"
                      },
                      "capabilities" : {
                        "usb-1.10" : "USB 1.1"
                      },
                      "children" : [ {
                        "id" : "usb:0",
                        "class" : "input",
                        "claimed" : true,
                        "handle" : "USB:2:2",
                        "description" : "Mouse",
                        "product" : "VMware Virtual USB Mouse",
                        "vendor" : "VMware",
                        "physid" : "1",
                        "businfo" : "usb@2:1",
                        "version" : "1.03",
                        "configuration" : {
                          "driver" : "usbhid",
                          "speed" : "12Mbit/s"
                        },
                        "capabilities" : {
                          "usb-1.10" : "USB 1.1"
                        }
                      }, {
                        "id" : "usb:1",
                        "class" : "bus",
                        "claimed" : true,
                        "handle" : "USB:2:3",
                        "description" : "USB hub",
                        "product" : "VMware Virtual USB Hub",
                        "vendor" : "VMware, Inc.",
                        "physid" : "2",
                        "businfo" : "usb@2:2",
                        "version" : "1.00",
                        "configuration" : {
                          "driver" : "hub",
                          "slots" : "7",
                          "speed" : "12Mbit/s"
                        },
                        "capabilities" : {
                          "usb-1.10" : "USB 1.1"
                        },
                        "children" : [ {
                          "id" : "usb",
                          "class" : "communication",
                          "claimed" : true,
                          "handle" : "USB:2:4",
                          "description" : "Bluetooth wireless interface",
                          "product" : "Virtual Bluetooth Adapter",
                          "vendor" : "VMware",
                          "physid" : "1",
                          "businfo" : "usb@2:2.1",
                          "version" : "1.00",
                          "serial" : "000650268328",
                          "configuration" : {
                            "driver" : "btusb",
                            "speed" : "12Mbit/s"
                          },
                          "capabilities" : {
                            "bluetooth" : "Bluetooth wireless radio",
                            "usb-2.00" : "USB 2.0"
                          }
                        } ]
                      } ]
                    } ]
                  }, {
                    "id" : "network",
                    "class" : "network",
                    "claimed" : true,
                    "handle" : "PCI:0000:02:01.0",
                    "description" : "Ethernet interface",
                    "product" : "82545EM Gigabit Ethernet Controller (Copper)",
                    "vendor" : "Intel Corporation",
                    "physid" : "1",
                    "businfo" : "pci@0000:02:01.0",
                    "logicalname" : "ens33",
                    "version" : "01",
                    "serial" : "00:0c:29:e2:9c:f9",
                    "units" : "bit/s",
                    "size" : 1000000000,
                    "capacity" : 1000000000,
                    "width" : 64,
                    "clock" : 66000000,
                    "configuration" : {
                      "autonegotiation" : "on",
                      "broadcast" : "yes",
                      "driver" : "e1000",
                      "driverversion" : "7.3.21-k8-NAPI",
                      "duplex" : "full",
                      "ip" : "192.168.195.132",
                      "latency" : "0",
                      "link" : "yes",
                      "mingnt" : "255",
                      "multicast" : "yes",
                      "port" : "twisted pair",
                      "speed" : "1Gbit/s"
                    },
                    "capabilities" : {
                      "pm" : "Power Management",
                      "pcix" : "PCI-X",
                      "bus_master" : "bus mastering",
                      "cap_list" : "PCI capabilities listing",
                      "rom" : "extension ROM",
                      "ethernet" : true,
                      "physical" : "Physical interface",
                      "logical" : "Logical interface",
                      "tp" : "twisted pair",
                      "10bt" : "10Mbit/s",
                      "10bt-fd" : "10Mbit/s (full duplex)",
                      "100bt" : "100Mbit/s",
                      "100bt-fd" : "100Mbit/s (full duplex)",
                      "1000bt-fd" : "1Gbit/s (full duplex)",
                      "autonegotiation" : "Auto-negotiation"
                    }
                  }, {
                    "id" : "multimedia",
                    "class" : "multimedia",
                    "claimed" : true,
                    "handle" : "PCI:0000:02:02.0",
                    "description" : "Multimedia audio controller",
                    "product" : "ES1371/ES1373 / Creative Labs CT2518",
                    "vendor" : "Ensoniq",
                    "physid" : "2",
                    "businfo" : "pci@0000:02:02.0",
                    "version" : "02",
                    "width" : 32,
                    "clock" : 33000000,
                    "configuration" : {
                      "driver" : "snd_ens1371",
                      "latency" : "64",
                      "maxlatency" : "255",
                      "mingnt" : "6"
                    },
                    "capabilities" : {
                      "bus_master" : "bus mastering",
                      "cap_list" : "PCI capabilities listing"
                    }
                  }, {
                    "id" : "usb:1",
                    "class" : "bus",
                    "claimed" : true,
                    "handle" : "PCI:0000:02:03.0",
                    "description" : "USB controller",
                    "product" : "USB2 EHCI Controller",
                    "vendor" : "VMware",
                    "physid" : "3",
                    "businfo" : "pci@0000:02:03.0",
                    "version" : "00",
                    "width" : 32,
                    "clock" : 33000000,
                    "configuration" : {
                      "driver" : "ehci-pci",
                      "latency" : "64",
                      "maxlatency" : "255",
                      "mingnt" : "6"
                    },
                    "capabilities" : {
                      "ehci" : "Enhanced Host Controller Interface (USB2)",
                      "bus_master" : "bus mastering",
                      "cap_list" : "PCI capabilities listing"
                    },
                    "children" : [ {
                      "id" : "usbhost",
                      "class" : "bus",
                      "claimed" : true,
                      "handle" : "USB:1:1",
                      "product" : "EHCI Host Controller",
                      "vendor" : "Linux 5.3.0-40-generic ehci_hcd",
                      "physid" : "1",
                      "businfo" : "usb@1",
                      "logicalname" : "usb1",
                      "version" : "5.03",
                      "configuration" : {
                        "driver" : "hub",
                        "slots" : "6",
                        "speed" : "480Mbit/s"
                      },
                      "capabilities" : {
                        "usb-2.00" : "USB 2.0"
                      }
                    } ]
                  }, {
                    "id" : "storage",
                    "class" : "storage",
                    "claimed" : true,
                    "handle" : "PCI:0000:02:05.0",
                    "description" : "SATA controller",
                    "product" : "SATA AHCI controller",
                    "vendor" : "VMware",
                    "physid" : "5",
                    "businfo" : "pci@0000:02:05.0",
                    "version" : "00",
                    "width" : 32,
                    "clock" : 66000000,
                    "configuration" : {
                      "driver" : "ahci",
                      "latency" : "64"
                    },
                    "capabilities" : {
                      "storage" : true,
                      "pm" : "Power Management",
                      "msi" : "Message Signalled Interrupts",
                      "ahci_1.0" : true,
                      "bus_master" : "bus mastering",
                      "cap_list" : "PCI capabilities listing",
                      "rom" : "extension ROM"
                    }
                  } ]
                }, {
                  "id" : "pci:2",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:03",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15",
                  "businfo" : "pci@0000:00:15.0",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:3",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:04",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.1",
                  "businfo" : "pci@0000:00:15.1",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:4",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:05",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.2",
                  "businfo" : "pci@0000:00:15.2",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:5",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:06",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.3",
                  "businfo" : "pci@0000:00:15.3",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:6",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:07",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.4",
                  "businfo" : "pci@0000:00:15.4",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:7",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:08",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.5",
                  "businfo" : "pci@0000:00:15.5",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:8",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:09",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.6",
                  "businfo" : "pci@0000:00:15.6",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:9",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:0a",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "15.7",
                  "businfo" : "pci@0000:00:15.7",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:10",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:0b",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16",
                  "businfo" : "pci@0000:00:16.0",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:11",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:0c",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.1",
                  "businfo" : "pci@0000:00:16.1",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:12",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:0d",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.2",
                  "businfo" : "pci@0000:00:16.2",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:13",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:0e",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.3",
                  "businfo" : "pci@0000:00:16.3",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:14",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:0f",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.4",
                  "businfo" : "pci@0000:00:16.4",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:15",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:10",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.5",
                  "businfo" : "pci@0000:00:16.5",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:16",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:11",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.6",
                  "businfo" : "pci@0000:00:16.6",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:17",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:12",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "16.7",
                  "businfo" : "pci@0000:00:16.7",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:18",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:13",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17",
                  "businfo" : "pci@0000:00:17.0",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:19",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:14",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.1",
                  "businfo" : "pci@0000:00:17.1",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:20",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:15",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.2",
                  "businfo" : "pci@0000:00:17.2",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:21",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:16",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.3",
                  "businfo" : "pci@0000:00:17.3",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:22",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:17",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.4",
                  "businfo" : "pci@0000:00:17.4",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:23",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:18",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.5",
                  "businfo" : "pci@0000:00:17.5",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:24",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:19",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.6",
                  "businfo" : "pci@0000:00:17.6",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:25",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:1a",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "17.7",
                  "businfo" : "pci@0000:00:17.7",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:26",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:1b",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18",
                  "businfo" : "pci@0000:00:18.0",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:27",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:1c",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.1",
                  "businfo" : "pci@0000:00:18.1",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:28",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:1d",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.2",
                  "businfo" : "pci@0000:00:18.2",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:29",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:1e",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.3",
                  "businfo" : "pci@0000:00:18.3",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:30",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:1f",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.4",
                  "businfo" : "pci@0000:00:18.4",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:31",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:20",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.5",
                  "businfo" : "pci@0000:00:18.5",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:32",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:21",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.6",
                  "businfo" : "pci@0000:00:18.6",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                }, {
                  "id" : "pci:33",
                  "class" : "bridge",
                  "claimed" : true,
                  "handle" : "PCIBUS:0000:22",
                  "description" : "PCI bridge",
                  "product" : "PCI Express Root Port",
                  "vendor" : "VMware",
                  "physid" : "18.7",
                  "businfo" : "pci@0000:00:18.7",
                  "version" : "01",
                  "width" : 32,
                  "clock" : 33000000,
                  "configuration" : {
                    "driver" : "pcieport"
                  },
                  "capabilities" : {
                    "pci" : true,
                    "pm" : "Power Management",
                    "pciexpress" : "PCI Express",
                    "msi" : "Message Signalled Interrupts",
                    "normal_decode" : true,
                    "bus_master" : "bus mastering",
                    "cap_list" : "PCI capabilities listing"
                  }
                } ]
              }, {
                "id" : "scsi:0",
                "class" : "storage",
                "claimed" : true,
                "physid" : "3",
                "logicalname" : "scsi2",
                "capabilities" : {
                  "emulated" : "Emulated device"
                },
                "children" : [ {
                  "id" : "cdrom",
                  "class" : "disk",
                  "claimed" : true,
                  "handle" : "SCSI:02:00:00:00",
                  "description" : "DVD-RAM writer",
                  "product" : "VMware SATA CD00",
                  "vendor" : "NECVMWar",
                  "physid" : "0.0.0",
                  "businfo" : "scsi@2:0.0.0",
                  "logicalname" : [ "/dev/cdrom", "/dev/cdrw", "/dev/sr0" ],
                  "dev" : "11:0",
                  "version" : "1.00",
                  "configuration" : {
                    "ansiversion" : "5",
                    "status" : "ready"
                  },
                  "capabilities" : {
                    "removable" : "support is removable",
                    "audio" : "Audio CD playback",
                    "cd-r" : "CD-R burning",
                    "cd-rw" : "CD-RW burning",
                    "dvd" : "DVD playback",
                    "dvd-r" : "DVD-R burning",
                    "dvd-ram" : "DVD-RAM burning"
                  },
                  "children" : [ {
                    "id" : "medium",
                    "class" : "disk",
                    "claimed" : true,
                    "physid" : "0",
                    "logicalname" : "/dev/cdrom",
                    "dev" : "11:0"
                  } ]
                } ]
              }, {
                "id" : "scsi:1",
                "class" : "storage",
                "claimed" : true,
                "physid" : "4",
                "logicalname" : "scsi3",
                "capabilities" : {
                  "emulated" : "Emulated device"
                },
                "children" : [ {
                  "id" : "cdrom",
                  "class" : "disk",
                  "claimed" : true,
                  "handle" : "SCSI:03:00:00:00",
                  "description" : "DVD-RAM writer",
                  "product" : "VMware SATA CD01",
                  "vendor" : "NECVMWar",
                  "physid" : "0.0.0",
                  "businfo" : "scsi@3:0.0.0",
                  "logicalname" : [ "/dev/dvd", "/dev/sr1" ],
                  "dev" : "11:1",
                  "version" : "1.00",
                  "configuration" : {
                    "ansiversion" : "5",
                    "status" : "ready"
                  },
                  "capabilities" : {
                    "removable" : "support is removable",
                    "audio" : "Audio CD playback",
                    "cd-r" : "CD-R burning",
                    "cd-rw" : "CD-RW burning",
                    "dvd" : "DVD playback",
                    "dvd-r" : "DVD-R burning",
                    "dvd-ram" : "DVD-RAM burning"
                  },
                  "children" : [ {
                    "id" : "medium",
                    "class" : "disk",
                    "claimed" : true,
                    "physid" : "0",
                    "logicalname" : "/dev/dvd",
                    "dev" : "11:1",
                    "configuration" : {
                      "signature" : "5092863d"
                    },
                    "capabilities" : {
                      "partitioned" : "Partitioned disk",
                      "partitioned:dos" : "MS-DOS partition table"
                    },
                    "children" : [ {
                      "id" : "volume",
                      "class" : "volume",
                      "description" : "Windows FAT volume",
                      "vendor" : "mkfs.fat",
                      "physid" : "2",
                      "version" : "FAT12",
                      "serial" : "833c-cdb0",
                      "size" : 18446744073709551104,
                      "configuration" : {
                        "FATs" : "2",
                        "filesystem" : "fat"
                      },
                      "capabilities" : {
                        "primary" : "Primary partition",
                        "boot" : "Contains boot code",
                        "fat" : "Windows FAT",
                        "initialized" : "initialized volume"
                      }
                    } ]
                  } ]
                } ]
              } ]
            }, {
              "id" : "remoteaccess",
              "class" : "system",
              "vendor" : "Intel",
              "physid" : "1",
              "capabilities" : {
                "inbound" : "receive inbound connections"
              }
            }, {
              "id" : "network",
              "class" : "network",
              "claimed" : true,
              "description" : "Ethernet interface",
              "physid" : "2",
              "logicalname" : "docker0",
              "serial" : "02:42:95:7f:33:0a",
              "configuration" : {
                "broadcast" : "yes",
                "driver" : "bridge",
                "driverversion" : "2.3",
                "firmware" : "N/A",
                "ip" : "172.17.0.1",
                "link" : "no",
                "multicast" : "yes"
              },
              "capabilities" : {
                "ethernet" : true,
                "physical" : "Physical interface"
              }
            } ]
          },
          "ip_addr" : [ {
            "ifindex" : 1,
            "ifname" : "lo",
            "flags" : [ "LOOPBACK", "UP", "LOWER_UP" ],
            "mtu" : 65536,
            "qdisc" : "noqueue",
            "operstate" : "UNKNOWN",
            "group" : "default",
            "txqlen" : 1000,
            "link_type" : "loopback",
            "address" : "00:00:00:00:00:00",
            "broadcast" : "00:00:00:00:00:00",
            "addr_info" : [ {
              "family" : "inet",
              "local" : "127.0.0.1",
              "prefixlen" : 8,
              "scope" : "host",
              "label" : "lo",
              "valid_life_time" : 4294967295,
              "preferred_life_time" : 4294967295
            }, {
              "family" : "inet6",
              "local" : "::1",
              "prefixlen" : 128,
              "scope" : "host",
              "valid_life_time" : 4294967295,
              "preferred_life_time" : 4294967295
            } ]
          }, {
            "ifindex" : 2,
            "ifname" : "ens33",
            "flags" : [ "BROADCAST", "MULTICAST", "UP", "LOWER_UP" ],
            "mtu" : 1500,
            "qdisc" : "fq_codel",
            "operstate" : "UP",
            "group" : "default",
            "txqlen" : 1000,
            "link_type" : "ether",
            "address" : "00:0c:29:e2:9c:f9",
            "broadcast" : "ff:ff:ff:ff:ff:ff",
            "addr_info" : [ {
              "family" : "inet",
              "local" : "192.168.195.132",
              "prefixlen" : 24,
              "broadcast" : "192.168.195.255",
              "scope" : "global",
              "noprefixroute" : true,
              "label" : "ens33",
              "valid_life_time" : 4294967295,
              "preferred_life_time" : 4294967295
            }, {
              "family" : "inet6",
              "local" : "fe80::a873:d9b0:7fd8:9f2",
              "prefixlen" : 64,
              "scope" : "link",
              "noprefixroute" : true,
              "valid_life_time" : 4294967295,
              "preferred_life_time" : 4294967295
            } ]
          }, {
            "ifindex" : 3,
            "ifname" : "docker0",
            "flags" : [ "NO-CARRIER", "BROADCAST", "MULTICAST", "UP" ],
            "mtu" : 1500,
            "qdisc" : "noqueue",
            "operstate" : "DOWN",
            "group" : "default",
            "link_type" : "ether",
            "address" : "02:42:95:7f:33:0a",
            "broadcast" : "ff:ff:ff:ff:ff:ff",
            "addr_info" : [ {
              "family" : "inet",
              "local" : "172.17.0.1",
              "prefixlen" : 16,
              "broadcast" : "172.17.255.255",
              "scope" : "global",
              "label" : "docker0",
              "valid_life_time" : 4294967295,
              "preferred_life_time" : 4294967295
            } ]
          } ],
          "ip_link" : [ {
            "ifindex" : 1,
            "ifname" : "lo",
            "flags" : [ "LOOPBACK", "UP", "LOWER_UP" ],
            "mtu" : 65536,
            "qdisc" : "noqueue",
            "operstate" : "UNKNOWN",
            "linkmode" : "DEFAULT",
            "group" : "default",
            "txqlen" : 1000,
            "link_type" : "loopback",
            "address" : "00:00:00:00:00:00",
            "broadcast" : "00:00:00:00:00:00"
          }, {
            "ifindex" : 2,
            "ifname" : "ens33",
            "flags" : [ "BROADCAST", "MULTICAST", "UP", "LOWER_UP" ],
            "mtu" : 1500,
            "qdisc" : "fq_codel",
            "operstate" : "UP",
            "linkmode" : "DEFAULT",
            "group" : "default",
            "txqlen" : 1000,
            "link_type" : "ether",
            "address" : "00:0c:29:e2:9c:f9",
            "broadcast" : "ff:ff:ff:ff:ff:ff"
          }, {
            "ifindex" : 3,
            "ifname" : "docker0",
            "flags" : [ "NO-CARRIER", "BROADCAST", "MULTICAST", "UP" ],
            "mtu" : 1500,
            "qdisc" : "noqueue",
            "operstate" : "DOWN",
            "linkmode" : "DEFAULT",
            "group" : "default",
            "link_type" : "ether",
            "address" : "02:42:95:7f:33:0a",
            "broadcast" : "ff:ff:ff:ff:ff:ff"
          } ],
          "mac_addr" : ["lo: 00:00:00:00:00:00", "eth0: fa:c9:d5:0d:b6:00", "eth1: fa:62:8f:21:24:01"]
        }
      }
    }
""".trimIndent()