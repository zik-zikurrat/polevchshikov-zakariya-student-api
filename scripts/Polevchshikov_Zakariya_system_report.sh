#!/bin/bash
# Polevchshikov_Zakariya_system_report.sh
# Collects basic system information and appends it to the personal log file.
#
# Usage:
#   ./scripts/Polevchshikov_Zakariya_system_report.sh [log_file]
#   $1 (optional) - path to the log file; default: logs/Polevchshikov_Zakariya_system_report.log

STUDENT_NAME="Polevchshikov Zakariya"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
LOG_FILE="${1:-$PROJECT_DIR/logs/Polevchshikov_Zakariya_system_report.log}"

mkdir -p "$(dirname "$LOG_FILE")"

report() {
    echo "=============================================="
    echo " System Report"
    echo "=============================================="
    echo "Student:          $STUDENT_NAME"
    echo "Date and time:    $(date '+%Y-%m-%d %H:%M:%S %Z')"
    echo "Username:         $(whoami)"
    echo "Hostname:         $(hostname)"
    echo "Operating system: $(uname -s)"
    echo "Kernel version:   $(uname -r)"
    echo
    echo "---- CPU ----"
    if [ -r /proc/cpuinfo ]; then
        # Linux
        model=$(lscpu 2>/dev/null | awk -F: '/Model name/ {print $2; exit}' | sed 's/^ *//')
        # Some virtual ARM CPUs report "-" as model name; fall back to the vendor
        case "$model" in
            ""|"-") model=$(lscpu 2>/dev/null | awk -F: '/Vendor ID/ {print $2; exit}' | sed 's/^ *//') ;;
        esac
        echo "Model: ${model:-unknown} ($(uname -m))"
        echo "Cores: $(nproc)"
    else
        # macOS
        echo "Model: $(sysctl -n machdep.cpu.brand_string)"
        echo "Cores: $(sysctl -n hw.ncpu)"
    fi
    echo
    echo "---- Memory ----"
    if command -v free >/dev/null 2>&1; then
        free -h
    else
        echo "Total: $(( $(sysctl -n hw.memsize) / 1024 / 1024 / 1024 )) GB"
        vm_stat | head -5
    fi
    echo
    echo "---- Disk usage ----"
    df -h /
    echo
    echo "Available disk space on /: $(df -h / | awk 'NR==2 {print $4}')"
    echo "=============================================="
    echo
}

# tee -a prints the report to the screen AND appends it to the log file
report | tee -a "$LOG_FILE"
echo "Report saved to: $LOG_FILE"
