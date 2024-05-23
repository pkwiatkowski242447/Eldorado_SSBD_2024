import {toast} from "@/components/ui/use-toast.ts";
import {t} from "i18next";

interface ApiError {
    response?: {
        data?: {
            message: string;
            violations?: string[];
        };
    };
}

function handleApiError(error: ApiError): void {

    if (error.response && error.response.data) {
        const {message, violations} = error.response.data;
        let violationMessages = "";
        if (violations) {
            violationMessages = violations.map((violation: string | string[]) => t(violation)).join(", ");
        }
        toast({
            variant: "destructive",
            title: t(message),
            description: violationMessages,
        });
    } else {
        toast({
            variant: "destructive",
            description: "Error",
        });
    }
}

export default handleApiError;