import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Loader2 } from "lucide-react";
import { useTranslation } from "react-i18next";
import {Input} from "@/components/ui/input.tsx";
import {Controller} from "react-hook-form";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {Switch} from "@/components/ui/switch.tsx";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";

// @ts-expect-error idk
function PersonalInfoForm({ formUserData, onSubmitUserData, isLoading, account, isAlertDialogOpen, setAlertDialogOpen, handleDialogAction}) {
    const { t } = useTranslation();

    return (
        <div>
            <Card className="mx-10 w-auto">
                <CardContent>
                    <Form {...formUserData}>
                            <form onSubmit={formUserData.handleSubmit(onSubmitUserData)}
                                  className="space-y-4">
                                <div className="grid gap-4 p-5">
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formUserData.control}
                                            name="name"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("accountSettings.personalInfo.firstName")}</FormLabel>
                                                    <FormControl>
                                                        <Input
                                                            {...field}
                                                            placeholder={account?.name}
                                                        />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formUserData.control}
                                            name="lastName"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("accountSettings.personalInfo.lastName")}</FormLabel>
                                                    <FormControl>
                                                        <Input
                                                            {...field}
                                                            placeholder={account?.lastname}
                                                        />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formUserData.control}
                                            name="phoneNumber"
                                            render={() => (
                                                <FormItem className="items-start">
                                                    <FormLabel
                                                        className="text-black text-center">{t("registerPage.phoneNumber")}</FormLabel>
                                                    <FormControl className="w-full">
                                                        <Controller
                                                            name="phoneNumber"
                                                            control={formUserData.control}
                                                            render={({field}) => (
                                                                <PhoneInput
                                                                    {...field}
                                                                    value={field.value}
                                                                    onChange={field.onChange}
                                                                    countries={['PL']}
                                                                    defaultCountry="PL"
                                                                    placeholder={account?.phoneNumber?.startsWith('+48') ? account.phoneNumber.slice(3).replace(/\B(?=(\d{3})+(?!\d))/g, " ") : account?.phoneNumber}

                                                                />
                                                            )}
                                                        />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formUserData.control}
                                            name="twoFactorAuth"
                                            render={() => (
                                                <FormField
                                                    control={formUserData.control}
                                                    name="twoFactorAuth"
                                                    render={({field}) => (
                                                        <FormItem>
                                                            <div className="flex flex-col">
                                                                <FormLabel className="text-black">
                                                                    {t("accountSettings.twoFactorAuth")}
                                                                </FormLabel>
                                                                <FormControl>
                                                                    <div
                                                                        className={"justify-center pt-5"}>
                                                                        <Switch {...field}
                                                                                defaultChecked={account?.twoFactorAuth}
                                                                                checked={field.value}
                                                                                onCheckedChange={field.onChange}
                                                                        />
                                                                    </div>
                                                                </FormControl>
                                                            </div>
                                                            <FormMessage/>
                                                        </FormItem>
                                                    )}
                                                />
                                            )}
                                        />
                                    </div>
                                    <Button type="submit" className="w-full pb-2"
                                            disabled={isLoading}>
                                        {isLoading ? (
                                            <>
                                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                            </>
                                        ) : (
                                            t("accountSettings.personalInfo.saveChanges")
                                        )}
                                    </Button>
                                </div>
                            </form>

                    </Form>
                </CardContent>
            </Card>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("accountSettings.confirmUserDataChange")}
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleDialogAction}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default PersonalInfoForm;